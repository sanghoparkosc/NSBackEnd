package com.nsmall.api.order.controller;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.nsmall.api.command.order.CancelOrderCommand;
import com.nsmall.api.command.order.ChangeOrderCommand;
import com.nsmall.api.command.order.ProcessPaymentCommand;
import com.nsmall.api.order.command.CreateOrderCommand;
import com.nsmall.api.order.dto.OrderCreationRequest;
import com.nsmall.api.order.dto.OrderModifyingRequest;
import com.nsmall.api.order.dto.PaymentProcessingRequest;

import org.axonframework.commandhandling.gateway.CommandGateway;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final CommandGateway commandGateway;

    @PostMapping("orders")
    public CompletableFuture<String> createOrder(@RequestBody OrderCreationRequest orderCreationRequest) {
        
        // 주문 생성 명령
        CreateOrderCommand command = CreateOrderCommand.builder()
        .orderId(UUID.randomUUID().toString())
        .userId(orderCreationRequest.getUserId())
        .productId(orderCreationRequest.getProductId())
        .quantity(orderCreationRequest.getQuantity())
        .address(orderCreationRequest.getAddress())
        .build();  

        return commandGateway.send(command);
    }

    @PutMapping("orders/{id}")
    public CompletableFuture<String> modifyOrder(@PathVariable String id, @RequestBody OrderModifyingRequest orderModifyingRequest) {
        
        // 주문 수정 명령
        ChangeOrderCommand command = ChangeOrderCommand.builder()
        .orderId(id)
        .quantity(orderModifyingRequest.getQuantity())
        .address(orderModifyingRequest.getAddress())
        .build(); 

        return commandGateway.sendAndWait(command);
    }

    @PutMapping("orders/cancel/{id}")
    public CompletableFuture<String> cancelOrder(@PathVariable String id) {
        
        // 주문 취소 명령
        CancelOrderCommand command = CancelOrderCommand.builder()
        .orderId(id)
        .build(); 

        return commandGateway.sendAndWait(command);
    }

    @PutMapping("orders/payment/{orderId}")
    public CompletableFuture<String> processPayment(@PathVariable String orderId, @RequestBody PaymentProcessingRequest paymentProcessingRequest) {
        
        // 결제 진행 명령
        ProcessPaymentCommand command = ProcessPaymentCommand.builder()
        .orderId(orderId)
        .paymentAmount(paymentProcessingRequest.getPaymentAmount())
        .build();

        return commandGateway.sendAndWait(command);
    }
}
