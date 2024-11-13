package com.sparta.nugulpayment.payment.dto.request;

import lombok.Getter;

@Getter
public class PostProcessRequest {
    private String paymentKey;
    private String orderId;
    private int amount;
    private long userId;

    public PostProcessRequest(String paymentKey, String orderId, int amount, long userId) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
        this.userId = userId;
    }
}
