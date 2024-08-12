package com.stylish.stylish.service;

import com.stylish.stylish.model.Order;
import com.stylish.stylish.model.PaymentRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.stylish.stylish.model.PaymentResponse;

@Log4j2
@Service
public class PaymentService {
    @Value("${payment.partner.key}")
    private String partnerKey;
    private final RestTemplate restTemplate;

    public PaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PaymentResponse sendPaymentRequest(String prime, Order order, long orderId) {
        String url = "https://sandbox.tappaysdk.com/tpc/payment/pay-by-prime";

        PaymentRequest requestDto = new PaymentRequest();
        requestDto.setPrime(prime);
        requestDto.setPartnerKey(partnerKey);
        requestDto.setMerchantId("AppWorksSchool_CTBC");
        requestDto.setDetails("TapPay Test");
        requestDto.setAmount(order.getTotal());

        PaymentRequest.Cardholder cardholder = new PaymentRequest.Cardholder();
        cardholder.setPhoneNumber(order.getRecipient().getPhone());
        cardholder.setName(order.getRecipient().getName());
        cardholder.setEmail(order.getRecipient().getEmail());
        cardholder.setAddress(order.getRecipient().getAddress());
        requestDto.setCardholder(cardholder);
        requestDto.setRemember(false);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("x-api-key", partnerKey);

        HttpEntity<PaymentRequest> requestEntity = new HttpEntity<>(requestDto, headers);

        ResponseEntity<PaymentResponse> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, PaymentResponse.class);
        PaymentResponse paymentResponse = response.getBody();
        return paymentResponse;
    }
}
