package com.sparta.nugulpayment.payment.dto.request;

import lombok.Getter;

@Getter
public class PostProcessRequest {
    private String paymentId;
    private String orderId;
    private int amount;
}
