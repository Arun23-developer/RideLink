package com.ridelink.rideservice.dto.response;

import com.ridelink.rideservice.enums.RideStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideStatusResponse {

    private Long rideId;
    private RideStatus status;
    private Long driverId;
    private LocalDateTime updatedAt;
}
