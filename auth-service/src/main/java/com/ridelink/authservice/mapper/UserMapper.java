package com.ridelink.authservice.mapper;

import com.ridelink.authservice.dto.request.RegisterRequest;
import com.ridelink.authservice.dto.response.UserResponse;
import com.ridelink.authservice.entity.User;
import com.ridelink.authservice.enums.Role;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(Role.valueOf(request.getRole().toUpperCase()))
                .build();
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
