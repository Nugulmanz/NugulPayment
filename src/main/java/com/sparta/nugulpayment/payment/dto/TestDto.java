package com.sparta.nugulpayment.payment.dto;

import lombok.Getter;

import java.util.Map;

@Getter
public class TestDto {
    private String type;
    private Object object;
    private Map<String, Object> data;  // data를 Map으로 설정
}
