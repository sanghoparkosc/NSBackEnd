package com.nsmall.api.util;

import com.nsmall.api.status.OrderDetailStatus;
import com.nsmall.api.status.OrderStatus;

public class StatusUtil {
    
    public static OrderDetailStatus getDetailStatus(OrderStatus status){
        OrderDetailStatus detailStatus;

        switch (status) {
            case CREATED:
                detailStatus = OrderDetailStatus.CREATED;
                break;
            
            case PRODUCT_READY:
                detailStatus = OrderDetailStatus.PRODUCT_READY;
                break;

            case PRODUCT_SHORT:
                detailStatus = OrderDetailStatus.PRODUCT_SHORT;
                break;

            case CANCELED:
                detailStatus = OrderDetailStatus.CANCELED;
                break;

            case FINISHED:
                detailStatus = OrderDetailStatus.FINISHED;
                break;  
            
            default:
                detailStatus = OrderDetailStatus.CREATED;
        }                 
        return detailStatus;
    }
}
