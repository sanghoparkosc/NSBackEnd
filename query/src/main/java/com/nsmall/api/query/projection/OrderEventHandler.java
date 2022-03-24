package com.nsmall.api.query.projection;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import com.nsmall.api.event.order.AddressChangedEvent;
import com.nsmall.api.event.order.OrderCanceledEvent;
import com.nsmall.api.event.order.OrderChangedEvent;
import com.nsmall.api.event.order.OrderCreatedEvent;
import com.nsmall.api.event.order.OrderFinishedEvent;
import com.nsmall.api.event.order.OrderQuantityChangedEvent;
import com.nsmall.api.event.order.OrderStatusChangedEvent;
import com.nsmall.api.query.entity.OrderDetailEntity;
import com.nsmall.api.query.repository.OrderDetailRepository;
import com.nsmall.api.status.OrderDetailStatus;
import com.nsmall.api.util.StatusUtil;
import com.nsmall.api.entity.UserEntity;
import com.nsmall.api.repository.UserRepository;
import com.nsmall.api.entity.ProductEntity;
import com.nsmall.api.repository.ProductRepository;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j 
public class OrderEventHandler {
    private  final OrderDetailRepository repository;
    private  final UserRepository userRepository;
    private  final ProductRepository productRepository;
    
    @EventHandler
    protected void on(OrderCreatedEvent event) {
        Optional<UserEntity> userEntity = userRepository.findById(event.getUserId());       
        String userName = userEntity.map(UserEntity::getName).orElse("이름 없음");
        
        Optional<ProductEntity> productEntity = productRepository.findById(event.getProductId());       
        String productName = productEntity.map(ProductEntity::getName).orElse("이름 없음");
        String productImage = productEntity.map(ProductEntity::getImage).orElse("이미지 없음");
        String productDesc = productEntity.map(ProductEntity::getDescription).orElse("설명 없음");
        Integer unitPrice = productEntity.map(ProductEntity::getUnitPrice).get();
        Integer paymentAmount = unitPrice * event.getQuantity();

        OrderDetailEntity entity = OrderDetailEntity.builder()
        .orderId(event.getOrderId())
        .userId(event.getUserId())
        .userName(userName)
        .productName(productName)
        .productImage(productImage)
        .productDesc(productDesc)
        .quantity(event.getQuantity())
        .address(event.getAddress())
        .orderDate(LocalDateTime.now())
        .paymentAmount(paymentAmount)
        .orderStatus(OrderDetailStatus.CREATED)
        .build();

        log.info(" orderDetailEntity 생성 = {}", entity);
        repository.save(entity);
    }

    @EventHandler
    protected void on(OrderChangedEvent event) {
        OrderDetailEntity detailEntity = repository.findById(event.getOrderId()).get();       
        detailEntity.setQuantity(event.getQuantity());
        detailEntity.setAddress(event.getAddress());
        repository.save(detailEntity);
    }

    @EventHandler
    protected void on(OrderQuantityChangedEvent event) {
        OrderDetailEntity detailEntity = repository.findById(event.getOrderId()).get();       
        detailEntity.setQuantity(event.getQuantity());
        repository.save(detailEntity);
    }

    @EventHandler
    protected void on(AddressChangedEvent event) {
        OrderDetailEntity detailEntity = repository.findById(event.getOrderId()).get();       
        detailEntity.setAddress(event.getAddress());
        repository.save(detailEntity);
    }

    @EventHandler
    protected void on(OrderStatusChangedEvent event) {
        OrderDetailEntity detailEntity = repository.findById(event.getOrderId()).get();              
        detailEntity.setOrderStatus(StatusUtil.getDetailStatus(event.getOrderStatus()));
        repository.save(detailEntity);
    }

    @EventHandler
    protected void on(OrderCanceledEvent event) {
        OrderDetailEntity detailEntity = repository.findById(event.getOrderId()).get();              
        detailEntity.setOrderStatus(OrderDetailStatus.CANCELED);
        detailEntity.setCancelDate(LocalDateTime.now());
        repository.save(detailEntity);
    }

    @EventHandler
    protected void on(OrderFinishedEvent event) {
        OrderDetailEntity detailEntity = repository.findById(event.getOrderId()).get();              
        detailEntity.setOrderStatus(OrderDetailStatus.FINISHED);
        detailEntity.setFinishedDate(LocalDateTime.now());
        repository.save(detailEntity);
    }
}