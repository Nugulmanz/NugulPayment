package com.sparta.nugulpayment.payment.controller;

import com.sparta.nugulpayment.payment.dto.request.PostProcessRequest;
import com.sparta.nugulpayment.payment.request.service.RequestService;
import com.sparta.nugulpayment.payment.result.service.ResultService;
import com.sparta.nugulpayment.payment.toss.TossService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class PaymentController {
    private final RequestService requestService;
    private final ResultService resultService;
    private final TossService tossService;


    // 결제 승인 API
    @RequestMapping(value = {"/confirm/widget", "/confirm/payment"})
    public ResponseEntity<JSONObject> confirmPayment(HttpServletRequest request, @RequestBody PostProcessRequest postProcessRequest) throws Exception {

        requestService.check(postProcessRequest);
        resultService.save(postProcessRequest);

        JSONObject response = tossService.requestPayment( postProcessRequest);
        int statusCode = response.containsKey("error") ? 400 : 200;

        return ResponseEntity.status(statusCode).body(response);
    }

//    @RequestMapping(value = "/confirm-billing")
//    public ResponseEntity<JSONObject> confirmBilling(@RequestBody String jsonBody) throws Exception {
//        JSONObject requestData = parseRequestData(jsonBody);
//        String billingKey = billingKeyMap.get(requestData.get("customerKey"));
//        JSONObject response = sendRequest(requestData, API_SECRET_KEY, "https://api.tosspayments.com/v1/billing/" + billingKey);
//        return ResponseEntity.status(response.containsKey("error") ? 400 : 200).body(response);
//    }
//
//    @RequestMapping(value = "/issue-billing-key")
//    public ResponseEntity<JSONObject> issueBillingKey(@RequestBody String jsonBody) throws Exception {
//        JSONObject requestData = parseRequestData(jsonBody);
//        JSONObject response = sendRequest(requestData, API_SECRET_KEY, "https://api.tosspayments.com/v1/billing/authorizations/issue");
//
//        if (!response.containsKey("error")) {
//            billingKeyMap.put((String) requestData.get("customerKey"), (String) response.get("billingKey"));
//        }
//
//        return ResponseEntity.status(response.containsKey("error") ? 400 : 200).body(response);
//    }
//
//    @RequestMapping(value = "/callback-auth", method = RequestMethod.GET)
//    public ResponseEntity<JSONObject> callbackAuth(@RequestParam String customerKey, @RequestParam String code) throws Exception {
//        JSONObject requestData = new JSONObject();
//        requestData.put("grantType", "AuthorizationCode");
//        requestData.put("customerKey", customerKey);
//        requestData.put("code", code);
//
//        String url = "https://api.tosspayments.com/v1/brandpay/authorizations/access-token";
//        JSONObject response = sendRequest(requestData, API_SECRET_KEY, url);
//
//        logger.info("Response Data: {}", response);
//
//        return ResponseEntity.status(response.containsKey("error") ? 400 : 200).body(response);
//    }
//
//    @RequestMapping(value = "/confirm/brandpay", method = RequestMethod.POST, consumes = "application/json")
//    public ResponseEntity<JSONObject> confirmBrandpay(@RequestBody String jsonBody) throws Exception {
//        JSONObject requestData = parseRequestData(jsonBody);
//        String url = "https://api.tosspayments.com/v1/brandpay/payments/confirm";
//        JSONObject response = sendRequest(requestData, API_SECRET_KEY, url);
//        return ResponseEntity.status(response.containsKey("error") ? 400 : 200).body(response);
//    }
//
//    private JSONObject parseRequestData(String jsonBody) {
//        try {
//            return (JSONObject) new JSONParser().parse(jsonBody);
//        } catch (ParseException e) {
//            logger.error("JSON Parsing Error", e);
//            return new JSONObject();
//        }
//    }
//
//    private JSONObject sendRequest(JSONObject requestData, String secretKey, String urlString) throws IOException {
//        HttpURLConnection connection = createConnection(secretKey, urlString);
//        try (OutputStream os = connection.getOutputStream()) {
//            os.write(requestData.toString().getBytes(StandardCharsets.UTF_8));
//        }
//
//        try (InputStream responseStream = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream();
//             Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {
//            return (JSONObject) new JSONParser().parse(reader);
//        } catch (Exception e) {
//            logger.error("Error reading response", e);
//            JSONObject errorResponse = new JSONObject();
//            errorResponse.put("error", "Error reading response");
//            return errorResponse;
//        }
//    }
//
//    private HttpURLConnection createConnection(String secretKey, String urlString) throws IOException {
//        URL url = new URL(urlString);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8)));
//        connection.setRequestProperty("Content-Type", "application/json");
//        connection.setRequestMethod("POST");
//        connection.setDoOutput(true);
//        return connection;
//    }

//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public String index() {
//        return "/widget/checkout";
//    }
//
//    @RequestMapping(value = "/fail", method = RequestMethod.GET)
//    public String failPayment(HttpServletRequest request, Model model) {
//        model.addAttribute("code", request.getParameter("code"));
//        model.addAttribute("message", request.getParameter("message"));
//        return "/fail";
//    }
}
