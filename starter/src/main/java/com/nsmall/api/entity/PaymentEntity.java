package com.nsmall.api.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.nsmall.api.status.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name="paymentInfo")
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {

    @Id    
    private String paymentId;    
    private String orderId;
    private String message;
    private Integer amount; 
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private LocalDateTime regDate;
    
}
