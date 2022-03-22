package com.nsmall.api.command.order;

import com.nsmall.api.status.OrderStatus;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Builder
@ToString
public class FinishOrderCommand {

    @TargetAggregateIdentifier
    private String orderId;
    
    private final String reason;
    private OrderStatus orderStatus;
    
}
