package com.sparta.nugulpayment.payment.request.controller;

import com.sparta.nugulpayment.payment.request.dto.saveRequest.SaveRequestRequest;
import com.sparta.nugulpayment.payment.request.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/events/v1")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    public void saveRequest(@RequestBody SaveRequestRequest reqDto){
        requestService.saveRequest(reqDto);
    }
}
