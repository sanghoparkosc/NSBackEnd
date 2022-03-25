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
public class PaymentResponse {
    private String paymentId;
    private Integer paymentAmount;
    private String resultCode;
    private String resultMsg;
}