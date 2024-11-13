package com.sparta.nugulpayment.payment.result.repository;

import com.sparta.nugulpayment.payment.result.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResultRepository extends JpaRepository<Result, Long> {
    Optional<Result> findByOrderId(String orderId);
}
