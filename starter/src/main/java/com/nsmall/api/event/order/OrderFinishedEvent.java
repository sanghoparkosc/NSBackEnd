package com.nsmall.api.event.order;

import com.nsmall.api.status.OrderStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class OrderFinishedEvent {
    private String orderId;   
    private String reason;
    private OrderStatus orderStatus;    
}
