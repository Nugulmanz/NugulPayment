package com.sparta.nugulpayment.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostProcessResponse {
    private String paymentId;
    private String orderId;
    private int amount;
}
