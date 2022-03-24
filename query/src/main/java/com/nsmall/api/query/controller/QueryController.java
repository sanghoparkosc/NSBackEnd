package com.nsmall.api.query.controller;

import java.util.List;

import com.nsmall.api.query.entity.OrderDetailEntity;
import com.nsmall.api.query.query.OrderIdQuery;
import com.nsmall.api.query.query.OrderListQuery;
import com.nsmall.api.query.query.UserIdQuery;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;

@RestController
@RequiredArgsConstructor
public class QueryController {
    private final QueryGateway gateway;
    
    @GetMapping("/orders")
    public List<OrderDetailEntity> orderList(){
        OrderListQuery query = OrderListQuery.builder().build();
        List<OrderDetailEntity> orderList = gateway.query(query, ResponseTypes.multipleInstancesOf(OrderDetailEntity.class)).join();
        return orderList;
    }

    @GetMapping("/orders/{orderId}")
    public OrderDetailEntity findByOrderId(@PathVariable String orderId){
        OrderIdQuery query = new OrderIdQuery(orderId);
        OrderDetailEntity detail = gateway.query(query, ResponseTypes.instanceOf(OrderDetailEntity.class)).join();
        return detail;
    }

    @GetMapping("/orders/user/{userId}")
    public List<OrderDetailEntity> findByUserId(@PathVariable String userId){
        UserIdQuery query = new UserIdQuery(userId);
        List<OrderDetailEntity> orderList = gateway.query(query, ResponseTypes.multipleInstancesOf(OrderDetailEntity.class)).join();
        return orderList;
    }
}