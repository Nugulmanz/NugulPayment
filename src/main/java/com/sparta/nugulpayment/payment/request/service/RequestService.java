package com.sparta.nugulpayment.payment.request.service;

import com.sparta.nugulpayment.payment.dto.request.PostProcessRequest;
import com.sparta.nugulpayment.payment.request.dto.saveRequest.SaveRequestRequest;
import com.sparta.nugulpayment.payment.request.entity.Request;
import com.sparta.nugulpayment.payment.request.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestService {
    private final RequestRepository requestRepository;

    /**
     * (비동기식) 초기 결제 정보 저장 메서드
     *
     * @param orderId : 주문 id
     * @param amount  : 가격
     */
    @Transactional
    public void postRequest(String orderId, long amount) {
        Request request = new Request(orderId, amount);
        requestRepository.save(request);
    }

    /**
     * (동기식) 초기 결제 정보 저장 메서드
     *
     * @param reqDto : orderId(주문 id), amount(가격), userId(유저 id)
     */
    @Transactional
    public void saveRequest(SaveRequestRequest reqDto) {
        Request request = new Request(reqDto);
        requestRepository.save(request);
    }

    /**
     * (동기식) 초기 결제 정보와 결제 인증 요청 정보의 데이터 비교 검증
     *
     * @param postProcessRequest : paymentKey, orderId, amount, userId
     */
    public void check(PostProcessRequest postProcessRequest) {
        requestRepository.findByOrderIdAndAmount(postProcessRequest.getOrderId(), postProcessRequest.getAmount())
                .orElseThrow(() -> new NullPointerException("없어요"));

    }

}
