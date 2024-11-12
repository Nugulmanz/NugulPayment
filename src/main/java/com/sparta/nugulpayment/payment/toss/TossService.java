package com.sparta.nugulpayment.payment.toss;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.nugulpayment.payment.common.TossUtil;
import com.sparta.nugulpayment.payment.dto.request.PostProcessRequest;
import com.sparta.nugulpayment.payment.request.service.RequestService;
import com.sparta.nugulpayment.payment.result.service.ResultService;
import com.sparta.nugulpayment.payment.transfer.service.TransferService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TossService {
    private static final String WIDGET_SECRET_KEY = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
    @Value("${toss.key.secret-key}")
    private static String API_SECRET_KEY;

    private final TossUtil tossUtil;

    public JSONObject requestPayment(HttpServletRequest request, PostProcessRequest postProcessRequest) throws Exception {
        String secretKey = request.getRequestURI().contains("/confirm/payment") ? API_SECRET_KEY : WIDGET_SECRET_KEY;
        ObjectMapper mapper = new ObjectMapper();

        String jsonString = mapper.writeValueAsString(postProcessRequest);
        return tossUtil.requestPaymentConfirm(jsonString, secretKey);
    }

}
