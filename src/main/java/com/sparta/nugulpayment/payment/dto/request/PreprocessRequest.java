package com.sparta.nugulpayment.payment.dto.request;

import lombok.Getter;

@Getter
public class PreprocessRequest {
    private String orderId;
    private int amount;
}
