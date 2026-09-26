package com.ridelink.rideservice.exception;

public class InvalidRideStatusException extends RuntimeException {

    public InvalidRideStatusException(String message) {
        super(message);
    }
}
