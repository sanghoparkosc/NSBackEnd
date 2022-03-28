package com.nsmall.api.order.projection;


import org.axonframework.eventhandling.EventHandler;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import com.nsmall.api.event.order.PaymentFailedEvent;
import com.nsmall.api.event.order.PaymentProcessStartedEvent;
import com.nsmall.api.event.order.PaymentSuccededEvent;
import com.nsmall.api.order.entity.PaymentEntity;
import com.nsmall.api.order.repository.PaymentRepository;
import com.nsmall.api.status.PaymentStatus;

@Component
@RequiredArgsConstructor
@Slf4j 

public class PaymentEventHandler {

    private final PaymentRepository repository;
    
    @EventHandler
    protected void on(PaymentProcessStartedEvent event) {
        
        PaymentEntity entity = PaymentEntity.builder()
        .paymentId(event.getPaymentId())
        .orderId(event.getOrderId())
        .amount(event.getPaymentAmount())
        .status(PaymentStatus.CREATED)
        .regDate(LocalDateTime.now())
        .build();

        log.info(" PaymentEntity 생성 = {}", entity);
        repository.save(entity);
    }

    @EventHandler
    protected void on(PaymentSuccededEvent event) {        
        PaymentEntity entity = repository.findByPaymentId(event.getPaymentId());   
        entity.setStatus(PaymentStatus.SUCCEDED);
        entity.setMessage(event.getResultMsg());
        repository.save(entity);
    }

    @EventHandler
    protected void on(PaymentFailedEvent event) {        
        PaymentEntity entity = repository.findByPaymentId(event.getPaymentId());   
        entity.setStatus(PaymentStatus.FAILED);
        entity.setMessage(event.getResultMsg());
        repository.save(entity);
    }
}
