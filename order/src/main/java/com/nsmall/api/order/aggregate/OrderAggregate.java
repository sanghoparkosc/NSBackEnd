package com.nsmall.api.order.aggregate;

import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import com.nsmall.api.event.order.OrderCreatedEvent;
import com.nsmall.api.order.command.CreateOrderCommand;
import com.nsmall.api.status.OrderStatus;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Aggregate
public class OrderAggregate {
    @AggregateIdentifier
    private String orderId;
    
    private String userId;
    private String productId;
    private Integer quantity;
    private String address;
    private OrderStatus orderStatus;    

    // 주문 생성 명령 처리
    @CommandHandler
    protected OrderAggregate(CreateOrderCommand command){
        OrderCreatedEvent event =  OrderCreatedEvent.builder()
            .orderId(command.getOrderId())
            .userId(command.getUserId())
            .productId(command.getProductId())
            .quantity(command.getQuantity())
            .address(command.getAddress())
            .orderStatus(OrderStatus.CREATED)
            .build();
        AggregateLifecycle.apply(event);             
    }

    // 주문 생성 이벤트소싱 처리
    @EventSourcingHandler
    protected void on(OrderCreatedEvent event){
        this.orderId = event.getOrderId();
        this.userId = event.getUserId();
        this.productId = event.getProductId();        
        this.quantity = event.getQuantity();
        this.address = event.getAddress();
        this.orderStatus = event.getOrderStatus();
    } 

}
