package com.nsmall.api.order.projection;

import org.axonframework.eventhandling.EventHandler;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import com.nsmall.api.entity.OrderEntity;
import com.nsmall.api.event.order.OrderCreatedEvent;
import com.nsmall.api.repository.OrderRepository;

@Component
@RequiredArgsConstructor
@Slf4j 
public class OrderEventHandler {
    
    private  final OrderRepository orderRepository;

    @EventHandler
    protected void on(OrderCreatedEvent event) {
        
        OrderEntity orderEntity = OrderEntity.builder()
        .orderId(event.getOrderId())
        .userId(event.getUserId())
        .productId(event.getProductId())
        .quantity(event.getQuantity())
        .orderStatus(event.getOrderStatus())
        .regDate(LocalDateTime.now())
        .build();

        log.info(" orderEntity 생성 = {}", orderEntity);
        orderRepository.save(orderEntity);
    }

}
