package com.nsmall.api.event.order;

import com.nsmall.api.status.OrderStatus;

public class OrderQuantityChangedEvent extends OrderChangedEvent {
    
    public OrderQuantityChangedEvent(){

    }

    public OrderQuantityChangedEvent(String orderId, Integer quantity, OrderStatus currentOrderStatus){
        this.orderId = orderId;
        this.quantity = quantity;
        this.currentOrderStatus = currentOrderStatus;
    }

}
