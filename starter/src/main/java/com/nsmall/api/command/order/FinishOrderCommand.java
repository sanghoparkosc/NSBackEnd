package com.nsmall.api.command.order;

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
    
}
