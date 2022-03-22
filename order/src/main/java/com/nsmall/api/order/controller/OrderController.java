package com.nsmall.api.order.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.nsmall.api.order.command.CreateOrderCommand;
import com.nsmall.api.order.dto.OrderCreationRequest;

import org.axonframework.commandhandling.gateway.CommandGateway;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
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
}
