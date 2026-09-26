package com.ridelink.rideservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRideRequest {

    @NotNull(message = "Passenger ID is required")
    private Long passengerId;

    @NotBlank(message = "Pickup location is required")
    private String pickupLocation;

    private Double pickupLatitude;

    private Double pickupLongitude;

    @NotBlank(message = "Dropoff location is required")
    private String dropoffLocation;

    private Double dropoffLatitude;

    private Double dropoffLongitude;

    private Double distanceKm;
}
