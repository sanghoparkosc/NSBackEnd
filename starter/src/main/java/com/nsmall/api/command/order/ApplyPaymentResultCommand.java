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
public class ApplyPaymentResultCommand {
    
    @TargetAggregateIdentifier
    private String orderId;
    
    private String paymentId;
    private Integer paymentAmount;
    private String resultCode;
    private String resultMsg;
    
}
