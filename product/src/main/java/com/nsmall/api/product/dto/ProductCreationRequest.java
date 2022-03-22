package com.nsmall.api.product.dto;

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
public class ProductCreationRequest {    
    private String name;
    private String image;
    private String description;
    private Integer quantity;
    private Integer unitPrice;
}
