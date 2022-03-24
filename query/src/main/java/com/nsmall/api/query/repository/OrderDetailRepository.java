package com.nsmall.api.query.repository;

import com.nsmall.api.query.entity.OrderDetailEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, String> {
    OrderDetailEntity findByOrderId(String orderId);
    List<OrderDetailEntity> findByUserId(String userId);
}