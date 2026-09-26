package com.ridelink.rideservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignDriverRequest {

    @NotNull(message = "Ride ID is required")
    private Long rideId;

    @NotNull(message = "Driver ID is required")
    private Long driverId;
}
