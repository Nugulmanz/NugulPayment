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
    private Long result_id;
    private String orderId;
    private String paymentKey;
    private Long user_id;
    private int amount;

    public Result(PostProcessRequest postProcessRequest){
        this.orderId = postProcessRequest.getOrderId();
        this.paymentKey = postProcessRequest.getPaymentKey();
        this.user_id = postProcessRequest.getUserId();
        this.amount = postProcessRequest.getAmount();
    }
}
