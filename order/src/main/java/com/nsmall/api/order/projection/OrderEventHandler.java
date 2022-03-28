package com.nsmall.api.order.projection;

import org.axonframework.eventhandling.EventHandler;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import com.nsmall.api.event.order.AddressChangedEvent;
import com.nsmall.api.event.order.OrderCanceledEvent;
import com.nsmall.api.event.order.OrderChangedEvent;
import com.nsmall.api.event.order.OrderCreatedEvent;
import com.nsmall.api.event.order.OrderFinishedEvent;
import com.nsmall.api.event.order.OrderQuantityChangedEvent;
import com.nsmall.api.event.order.OrderStatusChangedEvent;
import com.nsmall.api.event.order.PaymentFailedEvent;
import com.nsmall.api.event.order.PaymentSuccededEvent;
import com.nsmall.api.order.entity.OrderEntity;
import com.nsmall.api.order.repository.OrderRepository;
import com.nsmall.api.status.OrderStatus;

@Component
@RequiredArgsConstructor
@Slf4j 
public class OrderEventHandler {
    
    private final OrderRepository orderRepository;

    @EventHandler
    protected void on(OrderCreatedEvent event) {
        
        OrderEntity orderEntity = OrderEntity.builder()
        .orderId(event.getOrderId())
        .userId(event.getUserId())
        .productId(event.getProductId())
        .quantity(event.getQuantity())
        .address(event.getAddress())
        .orderStatus(event.getOrderStatus())
        .regDate(LocalDateTime.now())
        .build();

        log.info(" orderEntity 생성 = {}", orderEntity);
        orderRepository.save(orderEntity);
    }

    @EventHandler
    protected void on(OrderChangedEvent event) {        
        OrderEntity orderEntity = orderRepository.findByOrderId(event.getOrderId());       
        orderEntity.setQuantity(event.getQuantity());
        orderEntity.setAddress(event.getAddress());
        orderRepository.save(orderEntity);
    }

    @EventHandler
    protected void on(OrderQuantityChangedEvent event) {        
        OrderEntity orderEntity = orderRepository.findByOrderId(event.getOrderId());       
        orderEntity.setQuantity(event.getQuantity());
        orderRepository.save(orderEntity);
    }

    @EventHandler
    protected void on(AddressChangedEvent event) {        
        OrderEntity orderEntity = orderRepository.findByOrderId(event.getOrderId());       
        orderEntity.setAddress(event.getAddress());
        orderRepository.save(orderEntity);
    }

    @EventHandler
    protected void on(OrderStatusChangedEvent event) {        
        OrderEntity orderEntity = orderRepository.findByOrderId(event.getOrderId());       
        orderEntity.setOrderStatus(event.getOrderStatus());
        orderRepository.save(orderEntity);
    }

    @EventHandler
    protected void on(OrderCanceledEvent event) {        
        OrderEntity orderEntity = orderRepository.findByOrderId(event.getOrderId());       
        orderEntity.setOrderStatus(OrderStatus.CANCELED);
        orderEntity.setCancelDate(LocalDateTime.now());
        orderRepository.save(orderEntity);
    }

    @EventHandler
    protected void on(OrderFinishedEvent event) {        
        OrderEntity orderEntity = orderRepository.findByOrderId(event.getOrderId());       
        orderEntity.setOrderStatus(OrderStatus.FINISHED);
        orderRepository.save(orderEntity);
    }

    
    @EventHandler
    protected void on(PaymentSuccededEvent event) {        
        OrderEntity orderEntity = orderRepository.findByOrderId(event.getOrderId());       
        orderEntity.setOrderStatus(OrderStatus.PAYMENT_SUCCEDED);
        orderRepository.save(orderEntity);
    }

    @EventHandler
    protected void on(PaymentFailedEvent event) {        
        OrderEntity orderEntity = orderRepository.findByOrderId(event.getOrderId());       
        orderEntity.setOrderStatus(OrderStatus.PAYMENT_FAILED);
        orderRepository.save(orderEntity);
    }
    
}
