package com.nsmall.api.order.saga;


import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

import org.axonframework.modelling.saga.EndSaga;

import com.nsmall.api.command.order.ChangeOrderStatusCommand;
import com.nsmall.api.command.order.FinishOrderCommand;
import com.nsmall.api.command.product.CancelQuantityCommand;
import com.nsmall.api.command.product.ChangeQuantityCommand;
import com.nsmall.api.event.order.AddressChangedEvent;
import com.nsmall.api.event.order.OrderCanceledEvent;
import com.nsmall.api.event.order.OrderChangedEvent;
import com.nsmall.api.event.order.OrderCreatedEvent;
import com.nsmall.api.event.order.OrderFinishedEvent;
import com.nsmall.api.event.order.OrderQuantityChangedEvent;
import com.nsmall.api.status.OrderStatus;

@Saga
@Slf4j
public class OrderSaga {
    
    @Autowired
    private transient CommandGateway commandGateway;   
    
    private String orderId;
    private String productId;
    private Integer currentQuantity;
    private String currentAddress;
    private OrderStatus currentOrderStatus;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    protected void on(OrderCreatedEvent event){    
        
        this.orderId = event.getOrderId();
        this.productId = event.getProductId();
        this.currentQuantity = event.getQuantity();
        this.currentAddress = event.getAddress();
        this.currentOrderStatus = event.getOrderStatus();

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
       
        // 주문수량이 달라졌으면
        if(!this.currentQuantity.equals(event.getQuantity())){
            changeQuantity(event);
        }

        // 주소가 달라졌으면
        if(!this.currentAddress.equals(event.getAddress())){
            changeAddress(event);
        }
       
    } 

    protected void changeQuantity(OrderChangedEvent event){

        // 이미 상품 재고가 차감된 상태에서 주문 수량이 변경된 경우
        if(event.getCurrentOrderStatus() == OrderStatus.PRODUCT_READY){
            //먼저 기존 재고 보상 필요
            compensateProductQuantity();
        }
        
        this.currentQuantity = event.getQuantity();
        sendChangeQuantityCommand();
    }

    protected void sendChangeQuantityCommand(){

        //수량 변경 요청
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

                //결과에 따라 주문상태 변경 요청
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

    // 상품수량 보상 요청(동기 호출)
    protected void compensateProductQuantity(){      
        
        //기존 수량 취소 요청
        CancelQuantityCommand command = CancelQuantityCommand.builder()
            .orderId(this.orderId)
            .productId(this.productId)
            .quantity(this.currentQuantity)
            .build();

        commandGateway.sendAndWait(command);
    }

    protected void changeAddress(OrderChangedEvent event){
        
        this.currentAddress = event.getAddress();

        // TODO : 배송 주소 변경 명령
        
        
    }    

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    protected void on(OrderCanceledEvent event){
       
        // 이미 상품 재고가 차감된 상태에서 주문 취소가 된 경우
        if(event.getCurrentOrderStatus() == OrderStatus.PRODUCT_READY){
            //기존 재고 보상 필요
            compensateProductQuantity();
        }     
        
        log.info("#####################   OrderSaga 종료 - orderId : {} , CANCELED   ####################", event.getOrderId());
       
    } 

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void on(OrderFinishedEvent event){                
        log.info("#####################   OrderSaga 종료 - orderId : {} , FINISHED  ####################", event.getOrderId());
    }

}
