package com.sparta.nugulpayment.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PreprocessResponse {
    private String orderId;
    private Long amount;
    private long userId;
}
