package com.sparta.nugulpayment.payment.result.entity;

import com.sparta.nugulpayment.payment.common.Timestamped;
import com.sparta.nugulpayment.payment.dto.request.PostProcessRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Result extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resultId;
    private String orderId;
    private String paymentKey;
    private Long userId;
    private int amount;

    public Result(PostProcessRequest postProcessRequest){
        this.orderId = postProcessRequest.getOrderId();
        this.paymentKey = postProcessRequest.getPaymentKey();
        this.userId = postProcessRequest.getUserId();
        this.amount = postProcessRequest.getAmount();
    }

    public Result(String orderId, String paymentKey, Long userId, int amount){
        this.orderId = orderId;
        this.paymentKey = paymentKey;
        this.userId = userId;
        this.amount = amount;
    }
}
