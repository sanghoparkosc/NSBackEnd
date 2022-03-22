package com.nsmall.api.event.product;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class ProductQuantityChangedEvent {
    private String productId;
    private String orderId;
    private Integer quantity;
    private Integer unitPrice;
}
