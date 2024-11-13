//package com.sparta.nugulpayment.payment.test;
//
//import io.awspring.cloud.sqs.annotation.SqsListener;
//import org.springframework.stereotype.Service;
//
//@Service
//public class PaymentService {
//
//    /**
//     * Json 형태로 메시지를 받을 경우 Dto로 자동으로 변환되는지 확인
//     * 결과 : OK
//     */
////    @SqsListener(value = "${cloud.aws.sqs.queue.name}")
//    public void receiveMessage(TestDto testDto) {
//        System.out.println("Received type: " + testDto.getType());
//        System.out.println("Received object: " + testDto.getObject());
//        System.out.println("Received data: " + testDto.getData());
//
//        System.out.println("data containsKey false orderId : "+ testDto.getData().containsKey("orderId")); // true or false
//        System.out.println("data containsKey paymentKey : "+ testDto.getData().containsKey("paymentKey"));
//        System.out.println("data containsKey amount : "+ testDto.getData().containsKey("amount"));
//
//        System.out.println("data containsKey false orderId : "+ testDto.getData().get("orderId")); // value
//        System.out.println("data containsKey paymentKey : "+ testDto.getData().get("paymentKey"));
//        System.out.println("data containsKey amount : "+ testDto.getData().get("amount"));
//
//
//
////        switch (testDto.getType()) {
////            case "transfer":
////                System.out.println("Received type: " + testDto.getType());
////                testDto.getData() 이거 안에 내용 분리해서 사용할 수 있게 정제하기
////
////                if(testDto.getData().getOderId==false){
////                    에러 날리기
////                }
////                아니면 로직 쭉 나가기
////            case "reserve":
////                System.out.println("Received type: " + testDto.getType());
////
////        }
//    }
//}
