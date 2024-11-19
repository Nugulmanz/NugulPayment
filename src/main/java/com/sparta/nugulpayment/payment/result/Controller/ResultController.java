package com.sparta.nugulpayment.payment.result.Controller;

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

    // 결제 정보 반환 REST API
    @PostMapping("/info/payment")
    public ResponseEntity<JSONObject> getPaymentInfo (@RequestBody Map<String, Object> body) {
        // 요청 처리
        JSONObject paymentInfo = resultService.retrievePaymentInfo(
                (String) body.get("orderId"),
                ((Number) body.get("userId")).longValue()
        );

        return ResponseEntity.ok(paymentInfo);

    }
}
