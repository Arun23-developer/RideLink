package com.ridelink.rideservice.repository;

import com.ridelink.rideservice.entity.Ride;
import com.ridelink.rideservice.enums.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    List<Ride> findByPassengerIdOrderByRequestedAtDesc(Long passengerId);

    List<Ride> findByDriverIdOrderByRequestedAtDesc(Long driverId);

    List<Ride> findByStatus(RideStatus status);

    List<Ride> findByPassengerIdAndStatus(Long passengerId, RideStatus status);

    List<Ride> findByDriverIdAndStatus(Long driverId, RideStatus status);

    List<Ride> findByPassengerIdAndStatusIn(Long passengerId, List<RideStatus> statuses);

    List<Ride> findByDriverIdAndStatusIn(Long driverId, List<RideStatus> statuses);

    List<Ride> findAllByOrderByRequestedAtDesc();
}
