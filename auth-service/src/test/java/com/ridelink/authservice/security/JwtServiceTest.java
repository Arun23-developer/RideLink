package com.ridelink.authservice.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", "RideLinkSecretKeyForJWTTokenGenerationAndValidation2024SecureKey");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 86400000L);
    }

    @Test
    void testGenerateAndValidateToken() {
        UserDetails userDetails = new User("test@example.com", "password", Collections.emptyList());

        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        assertFalse(token.isBlank());

        String username = jwtService.extractUsername(token);
        assertEquals("test@example.com", username);

        boolean isValid = jwtService.isTokenValid(token, userDetails);
        assertTrue(isValid);
    }

    @Test
    void testTokenInvalidForDifferentUser() {
        UserDetails user1 = new User("user1@example.com", "password", Collections.emptyList());
        UserDetails user2 = new User("user2@example.com", "password", Collections.emptyList());

        String token = jwtService.generateToken(user1);
        boolean isValid = jwtService.isTokenValid(token, user2);
        assertFalse(isValid);
    }
}
