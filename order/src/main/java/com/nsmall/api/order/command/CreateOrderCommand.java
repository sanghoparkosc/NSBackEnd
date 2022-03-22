package com.nsmall.api.order.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.Builder;

@AllArgsConstructor
@Data
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
