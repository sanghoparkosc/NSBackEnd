package com.nsmall.api.event.order;

public class AddressChangedEvent extends OrderChangedEvent {
        
    public AddressChangedEvent(){

    }

    public AddressChangedEvent(String orderId, String address){
        this.orderId = orderId;
        this.address = address;
    }

}
