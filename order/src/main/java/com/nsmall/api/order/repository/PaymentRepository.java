package com.nsmall.api.order.repository;

import com.nsmall.api.order.entity.PaymentEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {
    PaymentEntity findByPaymentId(String id);
}
