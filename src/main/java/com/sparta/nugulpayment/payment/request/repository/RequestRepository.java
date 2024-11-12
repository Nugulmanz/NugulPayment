package com.sparta.nugulpayment.payment.request.repository;

import com.sparta.nugulpayment.payment.request.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Optional<Request> findByOrderIdAndAmount(String orderId, long amount);
}
