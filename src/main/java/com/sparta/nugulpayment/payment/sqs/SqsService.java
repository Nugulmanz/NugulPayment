package com.sparta.nugulpayment.payment.sqs;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.nugulpayment.config.AwsSqsConfig;
import com.sparta.nugulpayment.config.SQSProtocol;
import com.sparta.nugulpayment.payment.request.service.RequestService;
import com.sparta.nugulpayment.payment.sqs.dto.SQSApprovePayment;
import com.sparta.nugulpayment.payment.sqs.dto.SQSPreOrder;
import com.sparta.nugulpayment.payment.sqs.util.SqsUtility;
import com.sparta.nugulpayment.payment.test.TestDto;
import com.sparta.nugulpayment.payment.toss.TossService;
import com.sparta.nugulpayment.sns.service.SnsService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.DeleteMessageResponse;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

import java.io.DataInput;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class SqsService {
    private final RequestService requestService;
    private final TossService tossService;
    private final AwsSqsConfig awsSqsConfig;
    private final String INIT_REQUEST = "initRequest";
    private final String CONFIRM_REQUEST = "confirmRequest";
    private final SnsService snsService;
    private final SqsUtility sqsUtility;

    /**
     * 주문과 관련된 정보 전달(type=initRequest : 최초 결제 요청 데이터 저장
     * orderId, amount 포함 (userId도 추가해야 됨)
     * <p>
     * <p>
     * 결제 승인 요청 메세지 전달(type=confirmRequest) : 최초 저장 데이터로 데이터 검증 후, TossAPI로 결제 요청
     * orderId, amount, userId, paymentKey 포함
     */
    @SqsListener(value = "${cloud.aws.sqs.queue.name}")
    public void receiveMessage(Message message) throws Exception {
        System.out.println(message);
        Map<String ,MessageAttributeValue> messageAttribute = sqsUtility.parseMessage(message);


        final String type = sqsUtility.getType(messageAttribute);

        switch (type) {
            case SQSProtocol.TYPE_APPROVE_PAYMENT :
                SQSApprovePayment approvePaymentDto = new SQSApprovePayment();
                approvePaymentDto.fromSQSAttributes(messageAttribute);

                String paymentKey = approvePaymentDto.getPaymentKey();
                break;

            case SQSProtocol.TYPE_PRE_ORDER:
                SQSPreOrder preOrderDto = new SQSPreOrder();
                preOrderDto.fromSQSAttributes(messageAttribute);
                break;
        }
//        switch (sqsDto.getType()) {
//            case INIT_REQUEST : // 주문과 관련된 정보 전달
//                System.out.println("Received initRequest");
//
//                // data 안에 필요한 값이 없는 경우
//                if (!sqsDto.getData().containsKey("orderId") || !sqsDto.getData().containsKey("amount")) {
//                    // 예외 처리 로직 sms
//                    System.out.println("키가 없습니다");
//                    break;
//                }
//                requestService.saveInitRequest(sqsDto.getData().get("orderId").toString(), (int) sqsDto.getData().get("amount"));
//                System.out.println("save initRequest");
//                break;
//
//            case CONFIRM_REQUEST : // 결제 승인 요청 메세지 전달
//                System.out.println("Received confirmRequest");
//
//                // data 안에 필요한 값이 없는 경우
//                if (!sqsDto.getData().containsKey("orderId") ||
//                        !sqsDto.getData().containsKey("amount") ||
//                        !sqsDto.getData().containsKey("paymentKey")) {
//                    // 예외 처리 로직 sms
//                    System.out.println("키가 없습니다");
//                    break;
//                }
//
//                PostProcessRequest postProcessRequest = new PostProcessRequest(sqsDto.getData().get("paymentKey").toString(), sqsDto.getData().get("orderId").toString(), (int)sqsDto.getData().get("amount"), (long)sqsDto.getData().get("userId"));
//                JSONObject tossResponse = tossService.requestPayment(postProcessRequest);
//
//                // 토스에서 결제 거절된 경우
//                // 토스 서버에서 거절한 경우의 데이터를 알면 이 경우를 만들 수 있을 것 같음
//                // int statusCode = response.containsKey("error") ? 400 : 200; 이거랑 비슷할 듯
//
//                // 결제 승인은 됐지만, 토스 서버에서 결과가 반환되지 않은 경우(결제 정보로 토스 서버에 조회하는 api 필요)
//                // 결제 정보 조회하는 api 선님 거 확인해보기
//
//
//                // 토스 서버에서 응답이 전혀 오지 않는 경우
//                // 타임아웃, 몇번 시도할 것인지? 정하면 될 듯
//                // 리트라이는 3번까지 하기로 결정하고 4번쨰에는 결제 취소 요청하기(티켓서버 롤백)
//                // 타임아웃 하는 코드 알아보기
                    snsService.publishToPaymentTopic(convertSqsAttributesToSnsAttributes(message.messageAttributes()));
//
//
//                // 로직 성공 메세지 전송 sms
//
//                break;
//
//
//        }

        CompletableFuture<DeleteMessageResponse> response = awsSqsConfig.getSqsAsyncClient().deleteMessage(deleteMessageRequest ->
                deleteMessageRequest.queueUrl("https://sqs.ap-northeast-2.amazonaws.com/122610500734/NugulPayments.fifo")
                        .receiptHandle(message.receiptHandle())).whenComplete(((deleteMessageResponse, throwable) -> {
            if (throwable != null) {
                System.out.println(throwable.getMessage());
            } else {
                System.out.println(deleteMessageResponse.sdkHttpResponse().isSuccessful());
            };
        }));
        return;
    }

    public static Map<String, software.amazon.awssdk.services.sns.model.MessageAttributeValue> convertSqsAttributesToSnsAttributes(Map<String, MessageAttributeValue> sqsAttributes) {
        Map<String, software.amazon.awssdk.services.sns.model.MessageAttributeValue> snsAttributes = new HashMap<>();

        for (Map.Entry<String, MessageAttributeValue> entry : sqsAttributes.entrySet()) {
            String key = entry.getKey();
            MessageAttributeValue sqsValue = entry.getValue();

            // 변환된 SNS MessageAttributeValue를 생성
            software.amazon.awssdk.services.sns.model.MessageAttributeValue snsValue = software.amazon.awssdk.services.sns.model.MessageAttributeValue.builder()
                    .dataType(sqsValue.dataType())
                    .stringValue(sqsValue.stringValue())
                    .binaryValue(sqsValue.binaryValue())
                    .build();

            snsAttributes.put(key, snsValue);
        }

        return snsAttributes;
    }
}
