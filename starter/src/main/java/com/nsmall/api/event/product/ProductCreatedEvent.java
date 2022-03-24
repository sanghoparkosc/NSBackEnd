package com.nsmall.api.event.product;

import java.time.LocalDateTime;

import com.nsmall.api.status.ProductStatus;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductCreatedEvent {    
    private String id;    
    private String name;
    private String image;
    private String description;
    private Integer quantity;
    private Integer unitPrice;
    private LocalDateTime regDate; 
    private ProductStatus status;
}

