package com.sparta.nugulpayment.payment.sqs.dto;

import com.sparta.nugulpayment.config.SQSProtocol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SQSSuccessPayment  implements SQSDto {
    private String type;
    private long ticketId;

    @Override
    public void fromSQSAttributes(Map<String, MessageAttributeValue> attributes) {
        type = type = SQSProtocol.TYPE_SUCCESS_PAYMENT;
        ticketId = Long.parseLong(attributes.get(SQSProtocol.ATTRIBUTE_NAME_TICKET_ID).stringValue());
    }

    @Override
    public Map<String, software.amazon.awssdk.services.sns.model.MessageAttributeValue> toSNSAttributes() {
        Map<String, software.amazon.awssdk.services.sns.model.MessageAttributeValue> attributes = new HashMap<>();

        attributes.put(SQSProtocol.ATTRIBUTE_NAME_TYPE, software.amazon.awssdk.services.sns.model.MessageAttributeValue.builder()
                .dataType("String")
                .stringValue(SQSProtocol.TYPE_SUCCESS_PAYMENT)
                .build());

        attributes.put(SQSProtocol.ATTRIBUTE_NAME_TICKET_ID, software.amazon.awssdk.services.sns.model.MessageAttributeValue.builder()
                .dataType("Number")
                .stringValue(String.valueOf(ticketId))
                .build());

        return attributes;
    }
}
