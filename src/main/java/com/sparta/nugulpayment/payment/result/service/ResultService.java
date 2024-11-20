package com.sparta.nugulpayment.payment.result.service;

import com.sparta.nugulpayment.payment.dto.request.PostProcessRequest;
import com.sparta.nugulpayment.payment.result.entity.Result;
import com.sparta.nugulpayment.payment.result.repository.ResultRepository;
import com.sparta.nugulpayment.payment.sqs.dto.SQSApprovePayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResultService {
    private final ResultRepository resultRepository;

    /**
     * (비동기식) 결제 결과 저장 메서드
     *
     * @param approvePaymentDto : 결제 승인 요청 데이터 객체
     */
    @Transactional
    public void saveResult(SQSApprovePayment approvePaymentDto) {
        Result result = new Result(approvePaymentDto.getOrderId(), approvePaymentDto.getPaymentKey(), approvePaymentDto.getUserId(), (int) approvePaymentDto.getAmount());
        resultRepository.save(result);
    }

    /**
     * (동기식) 결제 결과 저장 메서드
     *
     * @param postProcessRequest : 결제 승인 요청 데이터 객체
     */
    @Transactional
    public void save(PostProcessRequest postProcessRequest) {
        Result result = new Result(postProcessRequest);
        resultRepository.save(result);
    }

}
