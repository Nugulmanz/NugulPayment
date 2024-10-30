package com.sparta.nugulpayment.payment.request.controller;

import com.sparta.nugulpayment.payment.dto.request.PreprocessRequest;
import com.sparta.nugulpayment.payment.dto.response.PreprocessResponse;
import com.sparta.nugulpayment.payment.request.dto.saveRequest.SaveRequestRequest;
import com.sparta.nugulpayment.payment.request.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    public void saveRequest(@RequestBody SaveRequestRequest reqDto){
        requestService.saveRequest(reqDto);
    }

    @ResponseBody
    @PostMapping("/preprocess")
    public ResponseEntity<PreprocessResponse> preprocess(@RequestBody SaveRequestRequest reqDto) throws Exception {
        // 데이터 저장
        requestService.saveRequest(reqDto);

        return ResponseEntity.ok(new PreprocessResponse(reqDto.getOrderId(), reqDto.getAmount()));
    }
}
