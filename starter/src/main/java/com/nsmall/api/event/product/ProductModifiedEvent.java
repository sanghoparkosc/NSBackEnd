package com.nsmall.api.event.product;

import java.time.LocalDateTime;

import com.nsmall.api.status.ProductStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductModifiedEvent {    
    private String name;
    private String image;
    private String description;
    private Integer quantity;
    private Integer unitPrice;
    private LocalDateTime modDate;     
    private ProductStatus status;
}
