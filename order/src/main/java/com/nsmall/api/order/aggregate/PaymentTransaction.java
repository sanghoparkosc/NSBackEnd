package com.nsmall.api.order.aggregate;
    
import org.axonframework.modelling.command.EntityId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.axonframework.eventsourcing.EventSourcingHandler;

import com.nsmall.api.status.PaymentStatus;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateLifecycle;
import com.nsmall.api.command.order.ApplyPaymentResultCommand;
import com.nsmall.api.constant.PaymentResultCode;
import com.nsmall.api.event.order.PaymentFailedEvent;
import com.nsmall.api.event.order.PaymentSuccededEvent;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Slf4j
public class PaymentTransaction {
    @EntityId
    private String paymentId;
    
    private String orderId;
    private String userId;
    private Integer amount;
    private PaymentStatus status;    


    // 결제 결과 적용 명령 처리
    @CommandHandler
    protected void handler(ApplyPaymentResultCommand command){    
        if(command.getResultCode().equals(PaymentResultCode.SUCCESS)){
            PaymentSuccededEvent event =  PaymentSuccededEvent.builder()
                .orderId(command.getOrderId())
                .paymentId(command.getPaymentId())    
                .userId(this.userId)
                .paymentAmount(command.getPaymentAmount())
                .resultCode(command.getResultCode())
                .resultMsg(command.getResultMsg())
                .build();
            AggregateLifecycle.apply(event);
        }else{
            PaymentFailedEvent event =  PaymentFailedEvent.builder()
                .orderId(command.getOrderId())
                .paymentId(command.getPaymentId())    
                .userId(this.userId)
                .paymentAmount(command.getPaymentAmount())
                .resultCode(command.getResultCode())
                .resultMsg(command.getResultMsg())
                .build();
            AggregateLifecycle.apply(event);
        }
    }

    // 결제 성공 이벤트소싱 처리
    @EventSourcingHandler
    protected void on(PaymentSuccededEvent event){
        this.status = PaymentStatus.SUCCEDED;
    }  

    // 결제 실패 이벤트소싱 처리
    @EventSourcingHandler
    protected void on(PaymentFailedEvent event){
        this.status = PaymentStatus.FAILED;
    }
}
