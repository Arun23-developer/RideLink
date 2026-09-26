package com.ridelink.rideservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "auth-service", url = "${services.auth-service.url:http://localhost:8081}")
public interface AuthClient {

    @GetMapping("/api/users/{id}")
    Map<String, Object> getUserById(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String token);
}
