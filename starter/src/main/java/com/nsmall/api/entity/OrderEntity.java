package com.nsmall.api.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.nsmall.api.status.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name="orderInfo")
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    @Id    
    private String orderId;    
    private String userId;    
    private String productId;    
    private Integer quantity;    
    private String address;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime regDate; 
    private String paymentId;   
    private String reason;   
    private LocalDateTime cancelDate; 
    private String shipmentId;
}