package com.nsmall.api.event.order;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class PaymentProcessStartedEvent {
    private String orderId;    
    private String paymentId;    
    private String userId;
    private Integer paymentAmount;    
}
