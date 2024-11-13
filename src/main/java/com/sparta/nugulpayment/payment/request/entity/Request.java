package com.sparta.nugulpayment.payment.request.entity;

import com.sparta.nugulpayment.payment.common.Timestamped;
import com.sparta.nugulpayment.payment.request.dto.saveRequest.SaveRequestRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Request extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long request_id;
    private String orderId;
    private long amount;

    public Request(SaveRequestRequest reqDto){
        this.orderId = reqDto.getOrderId();
        this.amount = reqDto.getAmount();
    }

    public Request(String orderId, long amount) {
        this.orderId = orderId;
        this.amount = amount;
    }
}
