package com.nsmall.api.query.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.nsmall.api.status.OrderDetailStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name="orderDetailInfo")
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailEntity {
    @Id
    private String orderId;
    private String userId;
    private String userName;
    private String productName;
    private String productImage;
    private String productDesc;
    private Integer quantity;
    private String address;
    private LocalDateTime orderDate;
    private Integer paymentAmount;
    private LocalDateTime paymentDate;
    private String reason;   
    private LocalDateTime cancelDate;
    private LocalDateTime deliveryDate;
    private LocalDateTime finishedDate;
    @Enumerated(EnumType.STRING)
    private OrderDetailStatus orderStatus;
}