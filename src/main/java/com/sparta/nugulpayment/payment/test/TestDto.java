package com.sparta.nugulpayment.payment.test;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

import java.util.Map;

@Getter
@JsonSerialize
public class TestDto {
    String Type;
    String MessageId;
    String SequenceNumber;
    String TopicArn;
    String Subject;
    String Message;
    String Timestamp;
    String UnsubscribeURL;
    Map<String, MessageAttributeValue> messageAttribute;
}
