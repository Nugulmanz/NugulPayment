package com.sparta.nugulpayment.payment.service;

import com.sparta.nugulpayment.payment.dto.TestDto;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    /**
     * Json 형태로 메시지를 받을 경우 Dto로 자동으로 변환되는지 확인
     * 결과 : OK
     */
    @SqsListener(value = "${cloud.aws.sqs.queue.name}")
    public void receiveMessage(TestDto testDto) {
        System.out.println("Received message: " + testDto.getId());
    }
}
