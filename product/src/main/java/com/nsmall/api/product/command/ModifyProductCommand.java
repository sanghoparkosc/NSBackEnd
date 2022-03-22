package com.nsmall.api.product.command;

import com.nsmall.api.status.ProductStatus;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Builder
@ToString
public class ModifyProductCommand {
    @TargetAggregateIdentifier
    private String id;
    
    private String name;
    private String image;
    private String description;
    private Integer quantity;
    private Integer unitPrice;
    private ProductStatus status;
    
}
