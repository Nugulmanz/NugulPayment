package com.sparta.nugulpayment.payment.result.controller;

import com.sparta.nugulpayment.payment.result.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ResultController {
    private final ResultService resultService;

    // 결제 정보 반환 API
    @PostMapping("/info/payment")
    public ResponseEntity<JSONObject> getPaymentInfo (@RequestBody Map<String, Object> body) {
        // 요청에서 필요한 데이터 추출
        String orderId = (String) body.get("orderId");
        Integer amount = (Integer) body.get("amount");
        Long userId = ((Number) body.get("userId")).longValue();

        // 결과 정보 조회 서비스 호출
        JSONObject paymentInfo = resultService.retrievePaymentInfo(orderId, amount, userId);

        return ResponseEntity.ok(paymentInfo);

    }
}
