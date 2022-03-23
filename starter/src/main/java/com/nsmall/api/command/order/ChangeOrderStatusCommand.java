package com.nsmall.api.command.order;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.nsmall.api.status.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.Builder;

@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ChangeOrderStatusCommand {
    
    @TargetAggregateIdentifier
    private String orderId;

    private OrderStatus orderStatus;
            
}
