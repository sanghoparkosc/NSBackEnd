package com.nsmall.api.product.controller;

import com.nsmall.api.product.command.ModifyProductCommand;
import com.nsmall.api.product.command.CreateProductCommand;
import com.nsmall.api.product.dto.ProductModifyingRequest;
import com.nsmall.api.product.dto.ProductCreationRequest;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final CommandGateway commandGateway;
    
    @PostMapping("products")
    public CompletableFuture<String> registerProduct(@RequestBody ProductCreationRequest req) {
        
        CreateProductCommand command = CreateProductCommand.builder()
        .id(UUID.randomUUID().toString())
        .name(req.getName())
        .unitPrice(req.getUnitPrice())
        .quantity(req.getQuantity())
        .image(req.getImage())
        .description(req.getDescription())
        .build();  

        return commandGateway.send(command);
    }
    
    @PutMapping("products/{id}")
    public String modifyProduct(@PathVariable String id, @RequestBody ProductModifyingRequest req) {
        
        ModifyProductCommand command = ModifyProductCommand.builder()
        .id(id)
        .name(req.getName())
        .unitPrice(req.getUnitPrice())
        .quantity(req.getQuantity())
        .image(req.getImage())
        .description(req.getDescription())
        .status(req.getStatus())
        .build();  

        return commandGateway.sendAndWait(command);
    }
}
