package com.ridelink.rideservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "driver-service", url = "${services.driver-service.url:http://localhost:8082}")
public interface DriverClient {

    @GetMapping("/api/drivers/available")
    Map<String, Object> getAvailableDrivers();

    @GetMapping("/api/drivers/{id}")
    Map<String, Object> getDriverById(@PathVariable("id") Long id);

    @PutMapping("/api/drivers/status")
    Map<String, Object> updateDriverStatus(@RequestBody Map<String, Object> request);
}
