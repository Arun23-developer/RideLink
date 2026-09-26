package com.ridelink.rideservice.service;

import com.ridelink.rideservice.dto.request.*;
import com.ridelink.rideservice.dto.response.RideResponse;
import com.ridelink.rideservice.dto.response.RideStatusResponse;
import com.ridelink.rideservice.entity.Ride;
import com.ridelink.rideservice.enums.RideStatus;
import com.ridelink.rideservice.exception.InvalidRideStatusException;
import com.ridelink.rideservice.exception.RideNotFoundException;
import com.ridelink.rideservice.mapper.RideMapper;
import com.ridelink.rideservice.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RideService {

    private final RideRepository rideRepository;
    private final RideMapper rideMapper;

    /**
     * Create a new ride request.
     * Sets the initial status to REQUESTED.
     */
    public RideResponse createRide(CreateRideRequest request) {
        log.info("Creating new ride request for passenger: {}", request.getPassengerId());

        Ride ride = rideMapper.toEntity(request);
        ride.setStatus(RideStatus.REQUESTED);

        Ride savedRide = rideRepository.save(ride);
        log.info("Ride created successfully with id: {}", savedRide.getId());

        return rideMapper.toResponse(savedRide);
    }

    /**
     * Get all rides ordered by most recent first.
     */
    @Transactional(readOnly = true)
    public List<RideResponse> getAllRides() {
        log.info("Fetching all rides");
        return rideRepository.findAllByOrderByRequestedAtDesc()
                .stream()
                .map(rideMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get a ride by its ID.
     */
    @Transactional(readOnly = true)
    public RideResponse getRideById(Long id) {
        log.info("Fetching ride with id: {}", id);
        Ride ride = findRideOrThrow(id);
        return rideMapper.toResponse(ride);
    }

    /**
     * Assign a driver to a ride.
     * Transition: REQUESTED -> ASSIGNED
     */
    public RideResponse assignDriver(AssignDriverRequest request) {
        log.info("Assigning driver {} to ride {}", request.getDriverId(), request.getRideId());

        Ride ride = findRideOrThrow(request.getRideId());

        if (ride.getStatus() != RideStatus.REQUESTED) {
            throw new InvalidRideStatusException(
                    "Invalid ride status transition. Ride must be in REQUESTED status to assign a driver. Current status: " + ride.getStatus());
        }

        ride.setDriverId(request.getDriverId());
        ride.setStatus(RideStatus.ASSIGNED);
        ride.setAssignedAt(LocalDateTime.now());

        Ride updatedRide = rideRepository.save(ride);
        log.info("Driver {} assigned to ride {} successfully", request.getDriverId(), request.getRideId());

        return rideMapper.toResponse(updatedRide);
    }

    /**
     * Driver accepts the ride.
     * Transition: ASSIGNED -> ACCEPTED
     */
    public RideResponse acceptRide(Long rideId) {
        log.info("Accepting ride: {}", rideId);

        Ride ride = findRideOrThrow(rideId);

        if (ride.getStatus() != RideStatus.ASSIGNED) {
            throw new InvalidRideStatusException(
                    "Invalid ride status transition. Ride must be in ASSIGNED status to accept. Current status: " + ride.getStatus());
        }

        ride.setStatus(RideStatus.ACCEPTED);
        ride.setAcceptedAt(LocalDateTime.now());

        Ride updatedRide = rideRepository.save(ride);
        log.info("Ride {} accepted successfully", rideId);

        return rideMapper.toResponse(updatedRide);
    }

    /**
     * Start the ride.
     * Transition: ACCEPTED -> IN_PROGRESS
     */
    public RideResponse startRide(StartRideRequest request) {
        log.info("Starting ride: {}", request.getRideId());

        Ride ride = findRideOrThrow(request.getRideId());

        if (ride.getStatus() != RideStatus.ACCEPTED) {
            throw new InvalidRideStatusException(
                    "Invalid ride status transition. Ride must be in ACCEPTED status to start. Current status: " + ride.getStatus());
        }

        ride.setStatus(RideStatus.IN_PROGRESS);
        ride.setStartedAt(LocalDateTime.now());

        Ride updatedRide = rideRepository.save(ride);
        log.info("Ride {} started successfully", request.getRideId());

        return rideMapper.toResponse(updatedRide);
    }

    /**
     * Complete the ride.
     * Transition: IN_PROGRESS -> COMPLETED
     */
    public RideResponse completeRide(CompleteRideRequest request) {
        log.info("Completing ride: {}", request.getRideId());

        Ride ride = findRideOrThrow(request.getRideId());

        if (ride.getStatus() != RideStatus.IN_PROGRESS) {
            throw new InvalidRideStatusException(
                    "Invalid ride status transition. Ride must be in IN_PROGRESS status to complete. Current status: " + ride.getStatus());
        }

        // Update final distance if provided
        if (request.getFinalDistanceKm() != null) {
            ride.setDistanceKm(request.getFinalDistanceKm());
        }

        ride.setStatus(RideStatus.COMPLETED);
        ride.setCompletedAt(LocalDateTime.now());

        Ride updatedRide = rideRepository.save(ride);
        log.info("Ride {} completed successfully", request.getRideId());

        return rideMapper.toResponse(updatedRide);
    }

    /**
     * Cancel a ride.
     * Transition: REQUESTED/ASSIGNED/ACCEPTED -> CANCELLED
     */
    public RideResponse cancelRide(CancelRideRequest request) {
        log.info("Cancelling ride: {}", request.getRideId());

        Ride ride = findRideOrThrow(request.getRideId());

        List<RideStatus> cancellableStatuses = Arrays.asList(
                RideStatus.REQUESTED, RideStatus.ASSIGNED, RideStatus.ACCEPTED);

        if (!cancellableStatuses.contains(ride.getStatus())) {
            throw new InvalidRideStatusException(
                    "Invalid ride status transition. Ride can only be cancelled when in REQUESTED, ASSIGNED, or ACCEPTED status. Current status: " + ride.getStatus());
        }

        ride.setStatus(RideStatus.CANCELLED);
        ride.setCancelledAt(LocalDateTime.now());
        ride.setCancellationReason(request.getCancellationReason());

        Ride updatedRide = rideRepository.save(ride);
        log.info("Ride {} cancelled successfully", request.getRideId());

        return rideMapper.toResponse(updatedRide);
    }

    /**
     * Get ride history (completed and cancelled rides).
     */
    @Transactional(readOnly = true)
    public List<RideResponse> getRideHistory() {
        log.info("Fetching ride history");
        List<RideStatus> historyStatuses = Arrays.asList(RideStatus.COMPLETED, RideStatus.CANCELLED);
        return rideRepository.findByStatus(RideStatus.COMPLETED)
                .stream()
                .map(rideMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all rides for a specific passenger.
     */
    @Transactional(readOnly = true)
    public List<RideResponse> getRidesByPassengerId(Long passengerId) {
        log.info("Fetching rides for passenger: {}", passengerId);
        return rideRepository.findByPassengerIdOrderByRequestedAtDesc(passengerId)
                .stream()
                .map(rideMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all rides for a specific driver.
     */
    @Transactional(readOnly = true)
    public List<RideResponse> getRidesByDriverId(Long driverId) {
        log.info("Fetching rides for driver: {}", driverId);
        return rideRepository.findByDriverIdOrderByRequestedAtDesc(driverId)
                .stream()
                .map(rideMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get the current status of a ride.
     */
    @Transactional(readOnly = true)
    public RideStatusResponse getRideStatus(Long rideId) {
        log.info("Fetching status for ride: {}", rideId);
        Ride ride = findRideOrThrow(rideId);
        return rideMapper.toStatusResponse(ride);
    }

    // -------- Helper Methods --------

    private Ride findRideOrThrow(Long rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException(rideId));
    }
}
