package com.sparta.nugulpayment.payment.sqs;

import com.sparta.nugulpayment.config.AwsSqsConfig;
import com.sparta.nugulpayment.config.SQSProtocol;
import com.sparta.nugulpayment.payment.request.service.RequestService;
import com.sparta.nugulpayment.payment.result.service.ResultService;
import com.sparta.nugulpayment.payment.sqs.dto.SQSApprovePayment;
import com.sparta.nugulpayment.payment.sqs.dto.SQSFailPayment;
import com.sparta.nugulpayment.payment.sqs.dto.SQSPreOrder;
import com.sparta.nugulpayment.payment.sqs.dto.SQSSuccessPayment;
import com.sparta.nugulpayment.payment.sqs.util.SqsUtility;
import com.sparta.nugulpayment.payment.toss.TossService;
import com.sparta.nugulpayment.sns.service.SnsService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SqsService {
    private final RequestService requestService;
    private final TossService tossService;
    private final AwsSqsConfig awsSqsConfig;
    private final SnsService snsService;
    private final SqsUtility sqsUtility;
    private final ResultService resultService;

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
                // dto 생성
                SQSApprovePayment approvePaymentDto = new SQSApprovePayment();
                approvePaymentDto.fromSQSAttributes(messageAttribute);

                JSONObject resTossPayment = tossService.requestTossPayment(approvePaymentDto);

                // 토스에서 결제 거절된 경우
//                // 토스 서버에서 거절한 경우의 데이터를 알면 이 경우를 만들 수 있을 것 같음
                // int statusCode = response.containsKey("error") ? 400 : 200; 이거랑 비슷할 듯
                if(resTossPayment.containsKey("error")){
                    SQSFailPayment failPaymentDto = new SQSFailPayment(SQSProtocol.TYPE_CANCEL_PAYMENT, approvePaymentDto.getTicketId(), "Toss에서 넘어오는 에러코드 담기");
                    snsService.publishToTicketTopic(failPaymentDto.toSNSAttributes());
                }else if(resTossPayment.containsKey("timeOut")){
                    // 결제 정보 조회 api 호출(구현 필요)
                    // 만약 결제 성공 시, successPayment 호출
                    // 결제 실패 시, failPayment 반환
                    // 이것도 timeOut 발생 했을 때, 이제 밑에 애들 진행

                    approvePaymentDto.setTryCount(approvePaymentDto.getTryCount() + 1);
                    if(approvePaymentDto.getTryCount()>3){
                        SQSFailPayment failPaymentDto = new SQSFailPayment(SQSProtocol.TYPE_CANCEL_PAYMENT, approvePaymentDto.getTicketId(), "Toss 결제 서버 오류 : 재시도 횟수 초과");
                        snsService.publishToTicketTopic(failPaymentDto.toSNSAttributes());
                    }
                    snsService.publishToPaymentTopic(approvePaymentDto.toSNSAttributes());
                }
                else {
                    resultService.saveResult(approvePaymentDto);

                    // 결제 승인 요청 전송 : sns
                    SQSSuccessPayment sqsSuccessPayment = new SQSSuccessPayment(
                            SQSProtocol.TYPE_SUCCESS_PAYMENT,
                            approvePaymentDto.getTicketId()
                    );

                    snsService.publishToTicketTopic(sqsSuccessPayment.toSNSAttributes()); // 실제 sns 보내는 코드
                }
                break;

            case SQSProtocol.TYPE_PRE_ORDER:
                SQSPreOrder preOrderDto = new SQSPreOrder();
                preOrderDto.fromSQSAttributes(messageAttribute);
                System.out.println("orderId : "+preOrderDto.getOrderId()+
                        ". type : " +
                        preOrderDto.getType()+
                        ". amount : " +
                        preOrderDto.getAmount());
                requestService.postRequest(preOrderDto.getOrderId(), preOrderDto.getAmount());
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
                    //snsService.publishToPaymentTopic(convertSqsAttributesToSnsAttributes(message.messageAttributes()));
//
//
//                // 로직 성공 메세지 전송 sms
//
//                break;
//
//
//        }

        sqsUtility.deleteMessage(awsSqsConfig.getSqsAsyncClient(), awsSqsConfig.getSqsQueueUrl(), message);
        return;
    }
}
