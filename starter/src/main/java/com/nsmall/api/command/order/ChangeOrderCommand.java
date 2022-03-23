package com.nsmall.api.command.order;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.Builder;

@AllArgsConstructor
@Getter
@Builder
@ToString
public class ChangeOrderCommand {
    
    @TargetAggregateIdentifier
    private String orderId;
    
    private Integer quantity;
    private String address;  
    
}
