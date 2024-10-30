package com.sparta.nugulpayment.payment.request.repository;

import com.sparta.nugulpayment.payment.request.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
