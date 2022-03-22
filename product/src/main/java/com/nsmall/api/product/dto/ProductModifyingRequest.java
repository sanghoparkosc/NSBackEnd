package com.nsmall.api.product.dto;

import com.nsmall.api.status.ProductStatus;

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
public class ProductModifyingRequest {    
    private String name;
    private String image;
    private String description;
    private Integer quantity;
    private Integer unitPrice;
    private ProductStatus status;
}
