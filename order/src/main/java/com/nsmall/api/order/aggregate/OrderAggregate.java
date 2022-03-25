package com.nsmall.api.order.aggregate;

import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.nsmall.api.command.order.CancelOrderCommand;
import com.nsmall.api.command.order.ChangeOrderCommand;
import com.nsmall.api.command.order.ChangeOrderStatusCommand;
import com.nsmall.api.command.order.FinishOrderCommand;
import com.nsmall.api.command.order.ProcessPaymentCommand;
import com.nsmall.api.event.order.AddressChangedEvent;
import com.nsmall.api.event.order.OrderCanceledEvent;
import com.nsmall.api.event.order.OrderChangedEvent;
import com.nsmall.api.event.order.OrderCreatedEvent;
import com.nsmall.api.event.order.OrderFinishedEvent;
import com.nsmall.api.event.order.OrderQuantityChangedEvent;
import com.nsmall.api.event.order.OrderStatusChangedEvent;
import com.nsmall.api.event.order.PaymentProcessStartedEvent;
import com.nsmall.api.order.command.CreateOrderCommand;
import com.nsmall.api.status.OrderStatus;
import com.nsmall.api.status.PaymentStatus;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.AggregateMember;
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

    @AggregateMember
    private List<PaymentTransaction> payments = new ArrayList<>();
    
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
            .currentOrderStatus(this.orderStatus)
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


    // 주문 취소 명령 처리
    @CommandHandler
    protected void handler(CancelOrderCommand command){     
        OrderCanceledEvent event = OrderCanceledEvent.builder()
            .orderId(command.getOrderId())
            .currentOrderStatus(this.orderStatus)
            .build();        
        
        AggregateLifecycle.apply(event); 
    }  
    
    // 주문 취소 이벤트소싱 처리
    @EventSourcingHandler
    protected void on(OrderCanceledEvent event){
        this.orderStatus = OrderStatus.CANCELED;
    } 


    // 주문 종료 명령 처리
    @CommandHandler
    protected void handler(FinishOrderCommand command){     
        OrderFinishedEvent event = OrderFinishedEvent.builder()
            .orderId(command.getOrderId())
            .build();        
        
        AggregateLifecycle.apply(event); 
    }  
    
    // 주문 종료 이벤트소싱 처리
    @EventSourcingHandler
    protected void on(OrderFinishedEvent event){
        this.orderStatus = OrderStatus.FINISHED;
    } 

    // 결제 진행 명령 처리
    @CommandHandler
    protected void handler(ProcessPaymentCommand command){    
        PaymentProcessStartedEvent event =  PaymentProcessStartedEvent.builder()
            .orderId(this.orderId)
            .paymentId(UUID.randomUUID().toString())    
            .userId(this.userId)
            .paymentAmount(command.getPaymentAmount())
            .build();
        AggregateLifecycle.apply(event);             
    }

    // 결제 요청 이벤트소싱 처리
    @EventSourcingHandler
    protected void on(PaymentProcessStartedEvent event){
        this.payments.add(new PaymentTransaction(event.getPaymentId(), event.getOrderId(), event.getUserId(), event.getPaymentAmount(), PaymentStatus.CREATED));        
    }      

}
