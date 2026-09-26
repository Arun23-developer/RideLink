package com.ridelink.rideservice.controller;

import com.ridelink.rideservice.dto.request.*;
import com.ridelink.rideservice.dto.response.ApiResponse;
import com.ridelink.rideservice.dto.response.RideResponse;
import com.ridelink.rideservice.dto.response.RideStatusResponse;
import com.ridelink.rideservice.service.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    /**
     * POST /api/rides - Create a new ride request
     */
    @PostMapping
    public ResponseEntity<ApiResponse<RideResponse>> createRide(
            @Valid @RequestBody CreateRideRequest request) {
        RideResponse ride = rideService.createRide(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Ride created successfully", ride));
    }

    /**
     * GET /api/rides - Get all rides
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<RideResponse>>> getAllRides() {
        List<RideResponse> rides = rideService.getAllRides();
        return ResponseEntity.ok(ApiResponse.success("Rides fetched successfully", rides));
    }

    /**
     * GET /api/rides/{id} - Get a ride by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RideResponse>> getRideById(@PathVariable Long id) {
        RideResponse ride = rideService.getRideById(id);
        return ResponseEntity.ok(ApiResponse.success("Ride fetched successfully", ride));
    }

    /**
     * PUT /api/rides/assign - Assign a driver to a ride
     */
    @PutMapping("/assign")
    public ResponseEntity<ApiResponse<RideResponse>> assignDriver(
            @Valid @RequestBody AssignDriverRequest request) {
        RideResponse ride = rideService.assignDriver(request);
        return ResponseEntity.ok(ApiResponse.success("Driver assigned successfully", ride));
    }

    /**
     * PUT /api/rides/accept - Driver accepts the ride
     */
    @PutMapping("/accept")
    public ResponseEntity<ApiResponse<RideResponse>> acceptRide(
            @RequestParam Long rideId) {
        RideResponse ride = rideService.acceptRide(rideId);
        return ResponseEntity.ok(ApiResponse.success("Ride accepted successfully", ride));
    }

    /**
     * PUT /api/rides/start - Start the ride
     */
    @PutMapping("/start")
    public ResponseEntity<ApiResponse<RideResponse>> startRide(
            @Valid @RequestBody StartRideRequest request) {
        RideResponse ride = rideService.startRide(request);
        return ResponseEntity.ok(ApiResponse.success("Ride started successfully", ride));
    }

    /**
     * PUT /api/rides/complete - Complete the ride
     */
    @PutMapping("/complete")
    public ResponseEntity<ApiResponse<RideResponse>> completeRide(
            @Valid @RequestBody CompleteRideRequest request) {
        RideResponse ride = rideService.completeRide(request);
        return ResponseEntity.ok(ApiResponse.success("Ride completed successfully", ride));
    }

    /**
     * PUT /api/rides/cancel - Cancel a ride
     */
    @PutMapping("/cancel")
    public ResponseEntity<ApiResponse<RideResponse>> cancelRide(
            @Valid @RequestBody CancelRideRequest request) {
        RideResponse ride = rideService.cancelRide(request);
        return ResponseEntity.ok(ApiResponse.success("Ride cancelled successfully", ride));
    }

    /**
     * GET /api/rides/history - Get ride history (completed rides)
     */
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<RideResponse>>> getRideHistory() {
        List<RideResponse> rides = rideService.getRideHistory();
        return ResponseEntity.ok(ApiResponse.success("Ride history fetched successfully", rides));
    }

    /**
     * GET /api/rides/passenger/{passengerId} - Get all rides for a passenger
     */
    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<ApiResponse<List<RideResponse>>> getRidesByPassenger(
            @PathVariable Long passengerId) {
        List<RideResponse> rides = rideService.getRidesByPassengerId(passengerId);
        return ResponseEntity.ok(ApiResponse.success("Passenger rides fetched successfully", rides));
    }

    /**
     * GET /api/rides/driver/{driverId} - Get all rides for a driver
     */
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<ApiResponse<List<RideResponse>>> getRidesByDriver(
            @PathVariable Long driverId) {
        List<RideResponse> rides = rideService.getRidesByDriverId(driverId);
        return ResponseEntity.ok(ApiResponse.success("Driver rides fetched successfully", rides));
    }

    /**
     * GET /api/rides/{id}/status - Get ride status
     */
    @GetMapping("/{id}/status")
    public ResponseEntity<ApiResponse<RideStatusResponse>> getRideStatus(@PathVariable Long id) {
        RideStatusResponse status = rideService.getRideStatus(id);
        return ResponseEntity.ok(ApiResponse.success("Ride status fetched successfully", status));
    }
}
