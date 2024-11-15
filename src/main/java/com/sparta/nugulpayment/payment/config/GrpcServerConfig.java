package com.sparta.nugulpayment.payment.config;

import com.sparta.nugulpayment.payment.result.gRPC.PaymentServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GrpcServerConfig {

    private Server server;

    @Bean
    public Server grpcServer(PaymentServiceImpl paymentServiceImpl) throws IOException {
        server = ServerBuilder.forPort(50052)
                .addService(paymentServiceImpl)
                .build();

        server.start();
        System.out.println("결제 gRPC 서버가 50052 포트에서 시작되었습니다.");

        return server;
    }

    @PreDestroy
    public void stopGrpcServer() {
        if (server != null) {
            server.shutdown();
            System.out.println("결제 gRPC 서버가 종료되었습니다.");
        }
    }
}
