package com.ridelink.authservice.service;

import com.ridelink.authservice.dto.request.LoginRequest;
import com.ridelink.authservice.dto.request.RegisterRequest;
import com.ridelink.authservice.dto.request.UpdateUserRequest;
import com.ridelink.authservice.dto.response.AuthResponse;
import com.ridelink.authservice.dto.response.UserResponse;

import java.util.List;

public interface AuthService {

    /**
     * Register a new user.
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Authenticate a user and return a JWT token.
     */
    AuthResponse login(LoginRequest request);

    /**
     * Get all users ordered by most recent first.
     */
    List<UserResponse> getAllUsers();

    /**
     * Get a user by ID.
     */
    UserResponse getUserById(Long id);

    /**
     * Update a user's details.
     */
    UserResponse updateUser(Long id, UpdateUserRequest request);

    /**
     * Delete a user by ID.
     */
    void deleteUser(Long id);
}
