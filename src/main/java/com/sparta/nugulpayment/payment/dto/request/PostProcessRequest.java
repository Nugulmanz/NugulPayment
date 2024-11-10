package com.sparta.nugulpayment.payment.dto.request;

import lombok.Getter;

@Getter
public class PostProcessRequest {
    private String paymentKey;
    private String orderId;
    private int amount;
    private long userId;
}
