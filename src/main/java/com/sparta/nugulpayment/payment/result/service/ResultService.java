package com.sparta.nugulpayment.payment.result.service;

import com.sparta.nugulpayment.payment.result.repository.ResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResultService {
    private final ResultRepository resultRepository;
}
