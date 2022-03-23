package com.nsmall.api.order.aggregate;

import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import com.nsmall.api.command.order.ChangeOrderCommand;
import com.nsmall.api.command.order.ChangeOrderStatusCommand;
import com.nsmall.api.event.order.AddressChangedEvent;
import com.nsmall.api.event.order.OrderChangedEvent;
import com.nsmall.api.event.order.OrderCreatedEvent;
import com.nsmall.api.event.order.OrderQuantityChangedEvent;
import com.nsmall.api.event.order.OrderStatusChangedEvent;
import com.nsmall.api.order.command.CreateOrderCommand;
import com.nsmall.api.status.OrderStatus;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@AllArgsConstructor
@Aggregate
@Slf4j
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

    // 주문 수정 명령 처리
    @CommandHandler
    protected void handler(ChangeOrderCommand command){     
        
        // 변경된 값에 따라 event 생성하여 apply 
        OrderChangedEvent event;
        if (!this.quantity.equals(command.getQuantity()) && this.address.equals(command.getAddress())){
            event =  new OrderQuantityChangedEvent(command.getOrderId(), command.getQuantity(), this.orderStatus);
        }else if (this.quantity.equals(command.getQuantity()) && !this.address.equals(command.getAddress())){
            event =  new AddressChangedEvent(command.getOrderId(), command.getAddress());
        }else {
            event =  OrderChangedEvent.builder()
            .orderId(command.getOrderId())
            .quantity(command.getQuantity())
            .address(command.getAddress())
            .orderStatus(this.orderStatus)
            .build();  
        }
       
        AggregateLifecycle.apply(event);
    }    

    // 주문 수정(수량) 이벤트소싱 처리
    @EventSourcingHandler
    protected void on(OrderQuantityChangedEvent event){
        this.quantity = event.getQuantity();
    } 

    // 주문 수정(주소) 이벤트소싱 처리
    @EventSourcingHandler
    protected void on(AddressChangedEvent event){
        this.address = event.getAddress();
    } 

    // 주문 수정(그외) 이벤트소싱 처리
    @EventSourcingHandler
    protected void on(OrderChangedEvent event){
        this.quantity = event.getQuantity();
        this.address = event.getAddress();
    } 

    
    // 주문상태 변경 명령 처리
    @CommandHandler
    protected void handler(ChangeOrderStatusCommand command){     
        OrderStatusChangedEvent event = OrderStatusChangedEvent.builder()
            .orderId(command.getOrderId())
            .orderStatus(command.getOrderStatus())
            .build();        
        
        AggregateLifecycle.apply(event);
    }    

    // 주문상태 변경 이벤트소싱 처리
    @EventSourcingHandler
    protected void on(OrderStatusChangedEvent event){
        this.orderStatus = event.getOrderStatus();
    } 

}
