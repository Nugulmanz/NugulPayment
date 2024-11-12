package com.sparta.nugulpayment.payment.request.service;

import com.sparta.nugulpayment.payment.dto.request.PostProcessRequest;
import com.sparta.nugulpayment.payment.request.dto.saveRequest.SaveRequestRequest;
import com.sparta.nugulpayment.payment.request.entity.Request;
import com.sparta.nugulpayment.payment.request.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestService {
    private final RequestRepository requestRepository;

    @Transactional
    public void saveRequest(SaveRequestRequest reqDto) {
        Request request = new Request(reqDto);
        requestRepository.save(request);
    }

    public void check(PostProcessRequest postProcessRequest) {
        requestRepository.findByOrderIdAndAmount(postProcessRequest.getOrderId(), postProcessRequest.getAmount())
                .orElseThrow(()-> new NullPointerException("없어요"));

    }
}