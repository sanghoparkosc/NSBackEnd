package com.nsmall.api.event.order;


import com.nsmall.api.status.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderStatusChangedEvent {
    
    private String orderId;
    private OrderStatus orderStatus;
    
}
