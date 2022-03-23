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
public class OrderChangedEvent {
    protected String orderId;
    protected Integer quantity;
    protected String address;
    protected OrderStatus orderStatus;
}
