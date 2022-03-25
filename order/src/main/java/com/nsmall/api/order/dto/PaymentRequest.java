package com.nsmall.api.order.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
@ToString
public class PaymentRequest {
    private String paymentId;
    private Integer paymentAmount;
}
