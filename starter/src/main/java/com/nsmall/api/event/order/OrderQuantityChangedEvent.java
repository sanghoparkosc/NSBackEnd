package com.nsmall.api.event.order;

import com.nsmall.api.status.OrderStatus;

public class OrderQuantityChangedEvent extends OrderChangedEvent {
    
    private OrderStatus orderStatus;
    
    public OrderQuantityChangedEvent(){

    }

    public OrderQuantityChangedEvent(String orderId, Integer quantity, OrderStatus orderStatus){
        this.orderId = orderId;
        this.quantity = quantity;
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus(){
        return this.orderStatus;
    }
}
