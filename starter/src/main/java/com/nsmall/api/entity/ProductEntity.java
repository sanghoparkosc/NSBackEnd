package com.nsmall.api.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.nsmall.api.status.ProductStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name="productInfo")
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id    
    private String id;    
    private String name;
    private String image;
    private String description;
    private Integer quantity; 
    private Integer unitPrice; 
    @Enumerated(EnumType.STRING)
    private ProductStatus status;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    
}
