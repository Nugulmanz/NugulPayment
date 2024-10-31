package com.sparta.nugulpayment.payment.request.dto.saveRequest;

import lombok.Getter;

@Getter
public class SaveRequestRequest {
    private String orderId;
    private long amount;
    private long userId;
}
