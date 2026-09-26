package com.ridelink.rideservice.mapper;

import com.ridelink.rideservice.dto.request.CreateRideRequest;
import com.ridelink.rideservice.dto.response.RideResponse;
import com.ridelink.rideservice.dto.response.RideStatusResponse;
import com.ridelink.rideservice.entity.Ride;
import com.ridelink.rideservice.enums.RideStatus;
import org.springframework.stereotype.Component;

@Component
public class RideMapper {

    public Ride toEntity(CreateRideRequest request) {
        return Ride.builder()
                .passengerId(request.getPassengerId())
                .pickupLocation(request.getPickupLocation())
                .pickupLatitude(request.getPickupLatitude())
                .pickupLongitude(request.getPickupLongitude())
                .dropoffLocation(request.getDropoffLocation())
                .dropoffLatitude(request.getDropoffLatitude())
                .dropoffLongitude(request.getDropoffLongitude())
                .distanceKm(request.getDistanceKm())
                .status(RideStatus.REQUESTED)
                .build();
    }

    public RideResponse toResponse(Ride ride) {
        return RideResponse.builder()
                .id(ride.getId())
                .passengerId(ride.getPassengerId())
                .driverId(ride.getDriverId())
                .pickupLocation(ride.getPickupLocation())
                .pickupLatitude(ride.getPickupLatitude())
                .pickupLongitude(ride.getPickupLongitude())
                .dropoffLocation(ride.getDropoffLocation())
                .dropoffLatitude(ride.getDropoffLatitude())
                .dropoffLongitude(ride.getDropoffLongitude())
                .status(ride.getStatus())
                .distanceKm(ride.getDistanceKm())
                .estimatedFare(ride.getEstimatedFare())
                .finalFare(ride.getFinalFare())
                .requestedAt(ride.getRequestedAt())
                .assignedAt(ride.getAssignedAt())
                .acceptedAt(ride.getAcceptedAt())
                .startedAt(ride.getStartedAt())
                .completedAt(ride.getCompletedAt())
                .cancelledAt(ride.getCancelledAt())
                .cancellationReason(ride.getCancellationReason())
                .build();
    }

    public RideStatusResponse toStatusResponse(Ride ride) {
        return RideStatusResponse.builder()
                .rideId(ride.getId())
                .status(ride.getStatus())
                .driverId(ride.getDriverId())
                .updatedAt(ride.getUpdatedAt())
                .build();
    }
}
