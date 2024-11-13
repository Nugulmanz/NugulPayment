package com.sparta.nugulpayment.payment.request.controller;

import com.sparta.nugulpayment.payment.dto.response.PreprocessResponse;
import com.sparta.nugulpayment.payment.request.dto.saveRequest.SaveRequestRequest;
import com.sparta.nugulpayment.payment.request.service.RequestService;
import com.sparta.nugulpayment.sns.service.SnsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;
    private final SnsService snsService;

    @ResponseBody
    @PostMapping("/preprocess")
    public ResponseEntity<PreprocessResponse> preprocess(@RequestBody SaveRequestRequest reqDto) throws Exception {
        // 데이터 저장
        requestService.saveRequest(reqDto);

        return ResponseEntity.ok(new PreprocessResponse(reqDto.getOrderId(), reqDto.getAmount(), reqDto.getUserId()));
    }

    @ResponseBody
    @PostMapping("/test")
    public ResponseEntity<?> test() throws Exception {
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put("type", MessageAttributeValue.builder()
                .dataType("String")
                .stringValue("initRequest").build());
        messageAttributes.put("amount", MessageAttributeValue.builder()
                .dataType("Number")
                .stringValue(String.valueOf(new Random().nextInt())).build());

        PublishResponse rp = snsService.publishToPaymentTopic(messageAttributes);
        System.out.println(rp.messageId());

        return ResponseEntity.ok().body(rp.messageId());
    }
}
