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

import com.nsmall.api.command.product.ChangeQuantityCommand;
import com.nsmall.api.event.order.OrderCreatedEvent;
import com.nsmall.api.event.order.OrderFinishedEvent;
import com.nsmall.api.event.product.ProductQuantityChangedEvent;

@Saga
@Slf4j
public class OrderSaga {
    
    @Autowired
    private transient CommandGateway commandGateway;        

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    protected void on(OrderCreatedEvent event){    
        
        //수량 변경 요청
        ChangeQuantityCommand command = ChangeQuantityCommand.builder()
            .orderId(event.getOrderId())
            .productId(event.getProductId())
            .quantity(event.getQuantity())
            .build();

        commandGateway.send(command, 
        new CommandCallback<ChangeQuantityCommand, Object>() {
            @Override
            public void onResult(CommandMessage<? extends ChangeQuantityCommand> commandMessage,
                    CommandResultMessage<? extends Object> commandResultMessage) {
                if(commandResultMessage.isExceptional()) {
                    // 수량 변경 중 에러로 인한 주문취소 요청 
                           
                }             
            }
        });     
                     
    }    

    //수량 변경 성공 수신
    @SagaEventHandler(associationProperty = "orderId")
    protected void on(ProductQuantityChangedEvent event){
       
       // 결제 요청
       
    } 

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void on(OrderFinishedEvent event){                
        log.info("#####################   OrderSaga 종료 - orderId : {}   ####################", event.getOrderId());
    }

}
