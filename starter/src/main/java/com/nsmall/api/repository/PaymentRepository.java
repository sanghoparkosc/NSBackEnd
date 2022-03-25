package com.nsmall.api.repository;


import java.util.Optional;

import com.nsmall.api.entity.PaymentEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {
    PaymentEntity findByPaymentId(String id);
}
