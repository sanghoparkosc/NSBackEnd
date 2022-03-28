package com.nsmall.api.command.order;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@ToString
public class CreateOrderCommand {
    
    @TargetAggregateIdentifier
    private String orderId;
    
    private String userId;
    private String productId;
    private Integer quantity;
    private String address;    
    
}
