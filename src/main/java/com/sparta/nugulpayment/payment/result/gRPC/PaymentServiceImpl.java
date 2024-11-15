package com.sparta.nugulpayment.payment.result.gRPC;

import com.example.payment.grpc.PaymentServiceGrpc;
import com.sparta.nugulpayment.payment.result.entity.Result;
import com.sparta.nugulpayment.payment.result.repository.ResultRepository;
import io.grpc.stub.StreamObserver;
import com.example.payment.grpc.PaymentServiceProto.PaymentRequest;
import com.example.payment.grpc.PaymentServiceProto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl extends PaymentServiceGrpc.PaymentServiceImplBase {

    private final ResultRepository resultRepository;

    // 데이터베이스에서 해당 결제 정보를 조회한 후, 조회한 데이터를 PaymentResponse로 반환
    @Override
    public void getPaymentInfo (PaymentRequest request, StreamObserver<PaymentResponse> responseObserver) {
        String orderId = request.getOrderId();
        long userId = request.getUserId();

        Result result = resultRepository.findByOrderIdAndUserId(orderId, userId);

        if (result != null) {
            // 결제 정보가 있을 경우 응답 생성
            PaymentResponse response = PaymentResponse.newBuilder()
                    .setOrderId(result.getOrderId())
                    .setUserId(result.getUserId())
                    .setAmount(result.getAmount())
                    .setSuccess(true)
                    .setMessage("결제 정보 조회를 성공하였습니다.")
                    .build();

            responseObserver.onNext(response);

        } else {
            // 결제 정보가 없을 경우 실패 응답
            PaymentResponse response = PaymentResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("해당 결제 정보를 찾을 수 없습니다.")
                    .build();

            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }
}
