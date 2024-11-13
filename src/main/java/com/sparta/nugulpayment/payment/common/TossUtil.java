package com.sparta.nugulpayment.payment.common;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Component
public class TossUtil {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public JSONObject requestPaymentConfirm(String jsonString, String secretKey) throws Exception {
        JSONObject response = sendRequest(parseRequestData(jsonString), secretKey, "https://api.tosspayments.com/v1/payments/confirm");
        return response;
    }

    private JSONObject parseRequestData(String jsonBody) {
        try {
            return (JSONObject) new JSONParser().parse(jsonBody);
        } catch (ParseException e) {
            logger.error("JSON Parsing Error", e);
            return new JSONObject();
        }
    }

    private JSONObject sendRequest(JSONObject requestData, String secretKey, String urlString) throws IOException {
        HttpURLConnection connection = createConnection(secretKey, urlString);
        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestData.toString().getBytes(StandardCharsets.UTF_8));
        }

        try (InputStream responseStream = connect(connection); // 1차 요청
             Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {
            return (JSONObject) new JSONParser().parse(reader);
        } catch (Exception e) {
            try (InputStream responseStream = connect(connection); // 2차 요청
                 Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {
                return (JSONObject) new JSONParser().parse(reader);
            }
            catch (Exception e2){
                logger.error("Error reading response", e2);
                JSONObject errorResponse = new JSONObject();
                errorResponse.put("timeOut", "Error reading response");
                return errorResponse;
            }
        }
    }
    private InputStream connect(HttpURLConnection connection) throws IOException {
        InputStream responseStream = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream();
        return responseStream;
    }

    private HttpURLConnection createConnection(String secretKey, String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8)));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setConnectTimeout(3000); // 3초 동안만 연결 유지
        return connection;
    }

}
