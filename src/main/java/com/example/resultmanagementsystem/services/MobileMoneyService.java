package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.model.FeePayment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Base64;

@Service
public class MobileMoneyService {

    private final RestTemplate restTemplate = new RestTemplate();

    // MTN MoMo API configuration - use @Value for externalized config
    @Value("${momo.api.url:https://sandbox.momodeveloper.mtn.com}")
    private String momoApiUrl;

    @Value("${momo.api.key}")
    private String momoApiKey;

    @Value("${momo.subscription.key}")
    private String momoSubscriptionKey;

    @Value("${momo.user.id}")
    private String momoUserId;

    // Orange Money API configuration
    @Value("${orange.api.url:https://api.orange.com}")
    private String orangeApiUrl;

    @Value("${orange.client.id}")
    private String orangeClientId;

    @Value("${orange.client.secret}")
    private String orangeClientSecret;

    @Value("${orange.merchant.account}")
    private String orangeMerchantAccount;

    // Cache for tokens (in production, use Redis or database)
    private String momoAccessToken;
    private long momoTokenExpiry;
    private String orangeAccessToken;
    private long orangeTokenExpiry;

    public String initiatePayment(String phoneNumber, Double amount, FeePayment.PaymentMethod method) {
        String transactionId = UUID.randomUUID().toString();

        try {
            if (method == FeePayment.PaymentMethod.MOMO) {
                return initiateMoMoPayment(phoneNumber, amount, transactionId);
            } else if (method == FeePayment.PaymentMethod.ORANGE_MONEY) {
                return initiateOrangeMoneyPayment(phoneNumber, amount, transactionId);
            }
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Payment initiation failed: HTTP " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new RuntimeException("Payment initiation failed: " + e.getMessage());
        }

        return transactionId;
    }


    private String initiateMoMoPayment(String phoneNumber, Double amount, String transactionId) {
        // Get access token first
        String accessToken = getMoMoAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("X-Reference-Id", transactionId);
        headers.set("X-Target-Environment", "sandbox"); // Change to "live" for production
        headers.set("Ocp-Apim-Subscription-Key", momoSubscriptionKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("amount", amount.toString());
        requestBody.put("currency", "XAF");
        requestBody.put("externalId", transactionId);
        requestBody.put("payer", Map.of("partyIdType", "MSISDN", "partyId", phoneNumber));
        requestBody.put("payerMessage", "School fee payment");
        requestBody.put("payeeNote", "Payment for school fees");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                momoApiUrl + "/collection/v1_0/requesttopay",
                request,
                String.class
        );

        // Check if request was accepted (should return 202)
        if (response.getStatusCode() != HttpStatus.ACCEPTED) {
            throw new RuntimeException("MoMo payment request failed with status: " + response.getStatusCode());
        }

        return transactionId;
    }


    private String getMoMoAccessToken() {
        // Check if token is still valid
        if (momoAccessToken != null && System.currentTimeMillis() < momoTokenExpiry) {
            return momoAccessToken;
        }

        // Create API user first (this should be done once during setup)
        createMoMoApiUser();

        // Get access token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Ocp-Apim-Subscription-Key", momoSubscriptionKey);

        // Basic auth with user ID and API key
        String credentials = momoUserId + ":" + momoApiKey;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        headers.set("Authorization", "Basic " + encodedCredentials);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                momoApiUrl + "/collection/token/",
                request,
                Map.class
        );

        Map<String, Object> responseBody = response.getBody();
        momoAccessToken = (String) responseBody.get("access_token");
        Integer expiresIn = (Integer) responseBody.get("expires_in");
        momoTokenExpiry = System.currentTimeMillis() + (expiresIn * 1000L);

        return momoAccessToken;
    }


    private void createMoMoApiUser() {
        // This creates a user in sandbox - should be done once
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Reference-Id", momoUserId);
        headers.set("Ocp-Apim-Subscription-Key", momoSubscriptionKey);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("providerCallbackHost", "your-callback-host.com");

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(
                    momoApiUrl + "/v1_0/apiuser",
                    request,
                    String.class
            );
        } catch (HttpClientErrorException e) {
            // User might already exist, that's OK
            if (e.getStatusCode() != HttpStatus.CONFLICT) {
                throw e;
            }
        }

        // Get API key for the user
        HttpHeaders keyHeaders = new HttpHeaders();
        keyHeaders.set("Ocp-Apim-Subscription-Key", momoSubscriptionKey);
        HttpEntity<String> keyRequest = new HttpEntity<>(keyHeaders);

        ResponseEntity<Map> keyResponse = restTemplate.postForEntity(
                momoApiUrl + "/v1_0/apiuser/" + momoUserId + "/apikey",
                keyRequest,
                Map.class
        );

        Map<String, Object> keyBody = keyResponse.getBody();
        // Store this API key securely
    }

    private String initiateOrangeMoneyPayment(String phoneNumber, Double amount, String transactionId) {
        String accessToken = getOrangeAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("merchant", Map.of("account", orangeMerchantAccount));
        requestBody.put("customer", Map.of("msisdn", phoneNumber));
        requestBody.put("amount", Map.of("value", amount.intValue(), "currency", "XAF"));
        requestBody.put("partner", Map.of("reference", transactionId));
        requestBody.put("description", "School fee payment");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                orangeApiUrl + "/orange-money-webpay/cm/v1/webpayment", // Note: cm for Cameroon
                request,
                Map.class
        );

        Map<String, Object> responseBody = response.getBody();

        if (!"SUCCESS".equals(responseBody.get("status"))) {
            throw new RuntimeException("Orange Money payment initiation failed: " + responseBody.get("message"));
        }

        return transactionId;
    }



    private String getOrangeAccessToken() {
        if (orangeAccessToken != null && System.currentTimeMillis() < orangeTokenExpiry) {
            return orangeAccessToken;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String credentials = orangeClientId + ":" + orangeClientSecret;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        headers.set("Authorization", "Basic " + encodedCredentials);

        String requestBody = "grant_type=client_credentials";
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                orangeApiUrl + "/oauth/v2/token",
                request,
                Map.class
        );

        Map<String, Object> responseBody = response.getBody();
        orangeAccessToken = (String) responseBody.get("access_token");
        Integer expiresIn = (Integer) responseBody.get("expires_in");
        orangeTokenExpiry = System.currentTimeMillis() + (expiresIn * 1000L);

        return orangeAccessToken;
    }


    public boolean verifyPayment(String transactionId, FeePayment.PaymentMethod method) {
        try {
            if (method == FeePayment.PaymentMethod.MOMO) {
                return verifyMoMoPayment(transactionId);
            } else if (method == FeePayment.PaymentMethod.ORANGE_MONEY) {
                return verifyOrangeMoneyPayment(transactionId);
            }
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Payment verification failed: " + e.getMessage());
            return false;
        }
        return false;
    }

    private boolean verifyMoMoPayment(String transactionId) {
        String accessToken = getMoMoAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("X-Target-Environment", "sandbox");
        headers.set("Ocp-Apim-Subscription-Key", momoSubscriptionKey);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                momoApiUrl + "/collection/v1_0/requesttopay/" + transactionId,
                HttpMethod.GET,
                request,
                Map.class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            return false;
        }

        Map<String, Object> responseBody = response.getBody();
        return "SUCCESSFUL".equals(responseBody.get("status"));
    }


    private boolean verifyOrangeMoneyPayment(String transactionId) {
        String accessToken = getOrangeAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                orangeApiUrl + "/orange-money-webpay/cm/v1/transactionstatus?orderId=" + transactionId,
                HttpMethod.GET,
                request,
                Map.class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            return false;
        }

        Map<String, Object> responseBody = response.getBody();
        return "SUCCESS".equals(responseBody.get("status"));
    }
}