package com.nsmall.api.query.projection;

import java.util.List;

import com.nsmall.api.query.entity.OrderDetailEntity;
import com.nsmall.api.query.query.OrderIdQuery;
import com.nsmall.api.query.query.OrderListQuery;
import com.nsmall.api.query.query.UserIdQuery;
import com.nsmall.api.query.repository.OrderDetailRepository;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderQueryHandler {
    private final OrderDetailRepository orderDetailRepository;

    @QueryHandler
    protected List<OrderDetailEntity> on(OrderListQuery query){
        return orderDetailRepository.findAll();
    }

    @QueryHandler
    protected OrderDetailEntity on(OrderIdQuery query){
        return orderDetailRepository.findById(query.getOrderId()).get();
    }

    @QueryHandler
    protected List<OrderDetailEntity> on(UserIdQuery query){
        return orderDetailRepository.findByUserId(query.getUserId());
    }
}