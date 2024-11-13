package com.sparta.nugulpayment.payment.sqs;

import lombok.Getter;

import java.util.Map;

@Getter
public class SqsDto {
    private String type;
    private Map<String, Object> data;  // data를 Map으로 설정
}
