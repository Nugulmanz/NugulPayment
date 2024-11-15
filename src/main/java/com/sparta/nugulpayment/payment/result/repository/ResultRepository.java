package com.sparta.nugulpayment.payment.result.repository;

import com.sparta.nugulpayment.payment.result.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ResultRepository extends JpaRepository<Result, Long> {
    Result findByOrderIdAndUserId(String orderId, long userId);
}
