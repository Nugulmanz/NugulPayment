package com.sparta.nugulpayment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NugulPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(NugulPaymentApplication.class, args);
    }

}
