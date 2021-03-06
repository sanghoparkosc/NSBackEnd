package com.nsmall.api.order.saga;


import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

import org.axonframework.modelling.saga.EndSaga;

import com.nsmall.api.command.order.ApplyPaymentResultCommand;
import com.nsmall.api.command.order.ChangeOrderStatusCommand;
import com.nsmall.api.command.product.CancelQuantityCommand;
import com.nsmall.api.command.product.ChangeQuantityCommand;
import com.nsmall.api.constant.PaymentResultCode;
import com.nsmall.api.event.order.AddressChangedEvent;
import com.nsmall.api.event.order.OrderCanceledEvent;
import com.nsmall.api.event.order.OrderChangedEvent;
import com.nsmall.api.event.order.OrderCreatedEvent;
import com.nsmall.api.event.order.OrderFinishedEvent;
import com.nsmall.api.event.order.OrderQuantityChangedEvent;
import com.nsmall.api.event.order.PaymentProcessStartedEvent;
import com.nsmall.api.order.dto.PaymentRequest;
import com.nsmall.api.order.dto.PaymentResponse;
import com.nsmall.api.status.OrderStatus;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;


@Saga
@Slf4j
public class OrderSaga {
    public static final String PAYMENT_DEADLINE ="payment-processing-deadline";
    
    @Autowired
    private transient CommandGateway commandGateway;   

    @Autowired
    private transient RestTemplate restTemplate;
    
    @Autowired
    private transient DeadlineManager deadlineManager;  

    private String orderId;
    private String productId;
    private Integer currentQuantity;
    private String currentAddress;
    

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    protected void on(OrderCreatedEvent event){    
        
        this.orderId = event.getOrderId();
        this.productId = event.getProductId();
        this.currentQuantity = event.getQuantity();
        this.currentAddress = event.getAddress();

        sendChangeQuantityCommand();

    }    

    
    @SagaEventHandler(associationProperty = "orderId")
    protected void on(OrderQuantityChangedEvent event){

        changeQuantity(event);
       
    } 

    @SagaEventHandler(associationProperty = "orderId")
    protected void on(AddressChangedEvent event){
       
        changeAddress(event);

    } 

    @SagaEventHandler(associationProperty = "orderId")
    protected void on(OrderChangedEvent event){
       
        // ??????????????? ???????????????
        if(!this.currentQuantity.equals(event.getQuantity())){
            changeQuantity(event);
        }

        // ????????? ???????????????
        if(!this.currentAddress.equals(event.getAddress())){
            changeAddress(event);
        }
       
    } 

    protected void changeQuantity(OrderChangedEvent event){

        // ?????? ?????? ????????? ????????? ???????????? ?????? ????????? ????????? ??????
        if(event.getCurrentOrderStatus() == OrderStatus.PRODUCT_READY){
            //?????? ?????? ?????? ?????? ??????
            compensateProductQuantity();
        }
        
        this.currentQuantity = event.getQuantity();
        sendChangeQuantityCommand();
    }

    protected void sendChangeQuantityCommand(){

        //?????? ?????? ??????
        ChangeQuantityCommand command = ChangeQuantityCommand.builder()
            .orderId(this.orderId)
            .productId(this.productId)
            .quantity(this.currentQuantity)
            .build();
        
        commandGateway.send(command, 
        new CommandCallback<ChangeQuantityCommand, Object>() {
            @Override
            public void onResult(CommandMessage<? extends ChangeQuantityCommand> commandMessage, CommandResultMessage<? extends Object> commandResultMessage) 
            {

                //????????? ?????? ???????????? ?????? ??????
                OrderStatus orderStatus;
                if(commandResultMessage.isExceptional()) {
                    orderStatus = OrderStatus.PRODUCT_SHORT;
                }else{
                    orderStatus = OrderStatus.PRODUCT_READY;
                }    
                ChangeOrderStatusCommand changeOrderStatusCommand = ChangeOrderStatusCommand.builder()
                .orderId(orderId)
                .orderStatus(orderStatus)
                .build();
                
                commandGateway.send(changeOrderStatusCommand);     

            }
        }); 
    }

    // ???????????? ?????? ??????(?????? ??????)
    protected void compensateProductQuantity(){      
        
        //?????? ?????? ?????? ??????
        CancelQuantityCommand command = CancelQuantityCommand.builder()
            .orderId(this.orderId)
            .productId(this.productId)
            .quantity(this.currentQuantity)
            .build();

        commandGateway.sendAndWait(command);
    }

    protected void changeAddress(OrderChangedEvent event){
        
        this.currentAddress = event.getAddress();

        // TODO : ?????? ?????? ?????? ??????
        
        
    }    

    @SagaEventHandler(associationProperty = "orderId")
    protected void on(PaymentProcessStartedEvent event){

        // ?????? ?????? deadline Time ??????  
        deadlineManager.schedule(Duration.of(5, ChronoUnit.SECONDS), PAYMENT_DEADLINE, event);
        
        // ?????? ?????? - ?????? ?????????(paymentTester.jar ??????)
        String paymentApiUri = "http://localhost:8200/payment";
        HttpHeaders headers = new HttpHeaders();
        PaymentRequest req = PaymentRequest.builder()
                                .paymentId(event.getPaymentId())
                                .paymentAmount(event.getPaymentAmount())
                                .build();
        HttpEntity<PaymentRequest> entity = new HttpEntity<PaymentRequest>(req ,headers);
        try {
            PaymentResponse res = restTemplate.postForObject(paymentApiUri, entity, PaymentResponse.class);
            log.info("payment request res = {}", res);

            // ?????? ?????? ???????????? ?????? ???????????? ?????? ?????? 
            deadlineManager.cancelAll(PAYMENT_DEADLINE);

            // ?????? ?????? ?????? ??????
            ApplyPaymentResultCommand command = ApplyPaymentResultCommand.builder()
            .orderId(event.getOrderId())
            .paymentId(event.getPaymentId())
            .paymentAmount(event.getPaymentAmount())
            .resultCode(res.getResultCode())
            .resultMsg(res.getResultMsg())
            .build();

            commandGateway.sendAndWait(command);
        } catch (ResourceAccessException e){
            // ?????? DeadlineManager??? ?????? ??????
        }
        
    } 

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    protected void on(OrderCanceledEvent event){
       
        // ?????? ?????? ????????? ????????? ???????????? ?????? ????????? ??? ??????
        if(event.getCurrentOrderStatus() == OrderStatus.PRODUCT_READY){
            //?????? ?????? ?????? ??????
            compensateProductQuantity();
        }     
        
        log.info("#####################   OrderSaga ?????? - orderId : {} , CANCELED   ####################", event.getOrderId());
       
    } 

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void on(OrderFinishedEvent event){                
        log.info("#####################   OrderSaga ?????? - orderId : {} , FINISHED  ####################", event.getOrderId());
    }


    // ?????? ?????? ??????????????????
    @DeadlineHandler(deadlineName = PAYMENT_DEADLINE)
    public void onPaymentDeadline(PaymentProcessStartedEvent event){
        // ?????? ?????? ????????? ?????? ??????
        ApplyPaymentResultCommand command = ApplyPaymentResultCommand.builder()
        .orderId(event.getOrderId())
        .paymentId(event.getPaymentId())
        .paymentAmount(event.getPaymentAmount())
        .resultCode(PaymentResultCode.TIMEOUT)
        .resultMsg("?????? ?????? ????????????")
        .build();

        commandGateway.sendAndWait(command);
    }

}
