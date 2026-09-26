package com.ridelink.rideservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StartRideRequest {

    @NotNull(message = "Ride ID is required")
    private Long rideId;
}
