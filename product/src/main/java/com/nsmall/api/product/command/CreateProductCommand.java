package com.nsmall.api.product.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Builder
@ToString
public class CreateProductCommand {
    @TargetAggregateIdentifier
    private String id;
    
    private String name;
    private String image;
    private String description;
    private Integer quantity;
    private Integer unitPrice;
}
