package com.nsmall.api.command.product;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class CancelQuantityCommand {
    
    @TargetAggregateIdentifier
    private String productId;
    
    private String orderId;
    private Integer quantity;
    
}