package com.sparta.nugulpayment.payment.result.controller;

import com.sparta.nugulpayment.payment.result.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/events/v1")
@RequiredArgsConstructor
public class ResultController {
    private final ResultService resultService;

    // 결제 결과 조회 api
}
