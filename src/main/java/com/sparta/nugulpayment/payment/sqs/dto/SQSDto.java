package com.sparta.nugulpayment.payment.sqs.dto;

import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

import java.util.Map;

public interface SQSDto {
    void fromSQSAttributes(Map<String, MessageAttributeValue> attributes);
    Map<String, software.amazon.awssdk.services.sns.model.MessageAttributeValue> toSNSAttributes();
}
