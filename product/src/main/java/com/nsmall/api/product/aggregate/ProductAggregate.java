package com.nsmall.api.product.aggregate;

import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import com.nsmall.api.product.command.ModifyProductCommand;
import com.nsmall.api.status.ProductStatus;
import com.nsmall.api.command.product.ChangeQuantityCommand;
import com.nsmall.api.event.product.ProductCreatedEvent;
import com.nsmall.api.event.product.ProductModifiedEvent;
import com.nsmall.api.event.product.ProductQuantityChangedEvent;
import com.nsmall.api.product.command.CreateProductCommand;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateLifecycle;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.time.LocalDateTime;

@Aggregate
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="productInfo")
public class ProductAggregate {
    @AggregateIdentifier
    @Id
    private String id;
    
    private String name;
    private String image;
    private String description;
    private Integer quantity;
    private Integer unitPrice;  
    private ProductStatus status;  
    private LocalDateTime regDate; 
    private LocalDateTime modDate; 

    // 상품 생성 명령 처리
    @CommandHandler
    public ProductAggregate(CreateProductCommand command){
        
        this.id = command.getId();
        this.name = command.getName();
        this.image = command.getImage();
        this.description = command.getDescription();
        this.unitPrice = command.getUnitPrice();
        this.quantity = command.getQuantity();
        this.regDate = LocalDateTime.now();
        this.status = ProductStatus.ACTIVE;

        ProductCreatedEvent event =  ProductCreatedEvent.builder()
            .id(this.id)
            .name(this.name)
            .image(this.image)
            .description(this.description)
            .unitPrice(this.unitPrice)
            .quantity(this.quantity)
            .regDate(this.regDate)
            .status(this.status)
            .build();
        
        AggregateLifecycle.apply(event);

    }

    // 상품 수정 명령 처리
    @CommandHandler
    protected void handler(ModifyProductCommand command) throws InterruptedException {            
        this.name = command.getName();
        this.image = command.getImage();
        this.description = command.getDescription();
        this.unitPrice = command.getUnitPrice();
        this.quantity = command.getQuantity();
        this.modDate = LocalDateTime.now();
        this.status = command.getStatus();

        ProductModifiedEvent event =  ProductModifiedEvent.builder()
            .name(this.name)
            .image(this.image)
            .description(this.description)
            .unitPrice(this.unitPrice)
            .quantity(this.quantity)
            .modDate(this.modDate)
            .status(this.status)
            .build();
        
        AggregateLifecycle.apply(event);
    }

    // 수량 변경 명령 처리
    @CommandHandler
    protected void handler(ChangeQuantityCommand command) throws InterruptedException {            
        
        if(this.getQuantity() < command.getQuantity()){
            throw new IllegalArgumentException("상품 재고 부족");
        }else{
            this.quantity = this.quantity - command.getQuantity();
        
            // 수량 변경 이벤트 생성
            ProductQuantityChangedEvent event = ProductQuantityChangedEvent.builder()
                .productId(command.getProductId())
                .orderId(command.getOrderId())
                .quantity(command.getQuantity())
                .unitPrice(this.unitPrice)
                .build();

            AggregateLifecycle.apply(event);
        }
    }
}
