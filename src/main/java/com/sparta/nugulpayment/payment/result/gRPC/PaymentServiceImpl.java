package com.sparta.nugulpayment.payment.result.gRPC;

import com.example.payment.grpc.PaymentServiceGrpc;
import com.sparta.nugulpayment.payment.result.entity.Result;
import com.sparta.nugulpayment.payment.result.repository.ResultRepository;
import io.grpc.stub.StreamObserver;
import com.example.payment.grpc.PaymentServiceProto.PaymentRequest;
import com.example.payment.grpc.PaymentServiceProto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class PaymentServiceImpl extends PaymentServiceGrpc.PaymentServiceImplBase {

    private final ResultRepository resultRepository;

    /**
     * gRPC 틍신으로 티켓서버에서 받은 request로 조회한 결제정보 데이터를 반환하는 메서드
     * @param request gRPC request로 받은 유저정보
     * @param responseObserver 결제정보 반환
     */
    @Override
    public void getPaymentInfo (PaymentRequest request, StreamObserver<PaymentResponse> responseObserver) {
        String orderId = request.getOrderId();
        long userId = request.getUserId();

        try {
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
        } catch (IllegalArgumentException e) {
            // 잘못된 요청 데이터 처리
            PaymentResponse errorResponse = PaymentResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("요청 데이터가 잘못되었습니다: " + e.getMessage())
                    .build();
            responseObserver.onNext(errorResponse);

        } catch (Exception e) {
            // 일반적인 서버 에러 처리
            PaymentResponse errorResponse = PaymentResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("서버 에러가 발생했습니다. 잠시 후 다시 시도해 주세요.")
                    .build();
            responseObserver.onNext(errorResponse);

        } finally {
            responseObserver.onCompleted();
        }
    }
}
