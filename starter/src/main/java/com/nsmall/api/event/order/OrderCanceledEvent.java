package com.nsmall.api.event.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.nsmall.api.status.OrderStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderCanceledEvent {
    protected String orderId;
    protected OrderStatus currentOrderStatus;    
}
