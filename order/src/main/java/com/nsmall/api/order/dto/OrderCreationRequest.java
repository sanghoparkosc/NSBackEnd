package com.nsmall.api.order.dto;
 
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Builder;
import lombok.ToString;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@Builder
@ToString
@NoArgsConstructor
public class OrderCreationRequest {
    private String userId;
    private String productId;
    private Integer quantity;
    private String address;
}