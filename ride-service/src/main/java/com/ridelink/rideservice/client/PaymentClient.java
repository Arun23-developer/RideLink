package com.ridelink.rideservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "payment-service", url = "${services.payment-service.url:http://localhost:8084}")
public interface PaymentClient {

    @PostMapping("/api/fare/estimate")
    Map<String, Object> estimateFare(@RequestBody Map<String, Object> request);

    @PostMapping("/api/fare/calculate")
    Map<String, Object> calculateFare(@RequestBody Map<String, Object> request);

    @PostMapping("/api/payments")
    Map<String, Object> createPayment(@RequestBody Map<String, Object> request);
}
