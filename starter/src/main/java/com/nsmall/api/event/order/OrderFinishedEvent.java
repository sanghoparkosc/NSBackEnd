package com.nsmall.api.event.order;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class OrderFinishedEvent {
    private String orderId;   
}
