package com.sparta.nugulpayment.payment.result.service;

import com.sparta.nugulpayment.payment.dto.request.PostProcessRequest;
import com.sparta.nugulpayment.payment.result.entity.Result;
import com.sparta.nugulpayment.payment.result.repository.ResultRepository;
import com.sparta.nugulpayment.payment.sqs.dto.SQSApprovePayment;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResultService {
    private final ResultRepository resultRepository;

    @Transactional
    public void saveResult(SQSApprovePayment approvePaymentDto){
        Result result = new Result(approvePaymentDto.getOrderId(), approvePaymentDto.getPaymentKey(), approvePaymentDto.getUserId(), (int)approvePaymentDto.getAmount());
        resultRepository.save(result);
    }

    @Transactional
    public void save(PostProcessRequest postProcessRequest){
        Result result = new Result(postProcessRequest);
        resultRepository.save(result);
    }

    // 결제 정보 반환 API
    public JSONObject retrievePaymentInfo(String orderId, Integer amount, Long userId) {
        Optional<Result> paymentResult = resultRepository.findByOrderId(orderId);

        if (paymentResult.isPresent()) {
            Result result = paymentResult.get();

            JSONObject paymentInfo = new JSONObject();
            paymentInfo.put("orderId", result.getOrderId());
            paymentInfo.put("amount", result.getAmount());
            paymentInfo.put("userId", result.getUserId());

            return paymentInfo;
        } else {
            throw new RuntimeException("해당 order Id의 결제 정보를 찾을 수 없습니다");
        }
    }

}
