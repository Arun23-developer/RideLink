package com.ridelink.authservice.service;

import com.ridelink.authservice.dto.request.LoginRequest;
import com.ridelink.authservice.dto.request.RegisterRequest;
import com.ridelink.authservice.dto.response.AuthResponse;
import com.ridelink.authservice.dto.response.UserResponse;
import com.ridelink.authservice.entity.User;
import com.ridelink.authservice.enums.Role;
import com.ridelink.authservice.exception.InvalidCredentialsException;
import com.ridelink.authservice.exception.UserAlreadyExistsException;
import com.ridelink.authservice.mapper.UserMapper;
import com.ridelink.authservice.repository.UserRepository;
import com.ridelink.authservice.security.JwtService;
import com.ridelink.authservice.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthServiceImpl authService;

    private User sampleUser;
    private UserResponse sampleUserResponse;
    private UserDetails sampleUserDetails;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .password("encoded_pass")
                .phone("1234567890")
                .role(Role.PASSENGER)
                .enabled(true)
                .build();

        sampleUserResponse = UserResponse.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .role(Role.PASSENGER)
                .enabled(true)
                .build();

        sampleUserDetails = new org.springframework.security.core.userdetails.User(
                "john@example.com", "encoded_pass", Collections.emptyList());
    }

    @Test
    void testRegisterSuccess() {
        RegisterRequest request = RegisterRequest.builder()
                .name("John Doe")
                .email("john@example.com")
                .password("password123")
                .phone("1234567890")
                .role("PASSENGER")
                .build();

        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(userRepository.existsByPhone("1234567890")).thenReturn(false);
        when(userMapper.toEntity(request)).thenReturn(sampleUser);
        when(passwordEncoder.encode("password123")).thenReturn("encoded_pass");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);
        when(userDetailsService.loadUserByUsername("john@example.com")).thenReturn(sampleUserDetails);
        when(jwtService.generateToken(sampleUserDetails)).thenReturn("mock.jwt.token");
        when(userMapper.toResponse(sampleUser)).thenReturn(sampleUserResponse);

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("mock.jwt.token", response.getToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals("john@example.com", response.getUser().getEmail());
    }

    @Test
    void testRegisterEmailAlreadyExists() {
        RegisterRequest request = RegisterRequest.builder()
                .name("John Doe")
                .email("john@example.com")
                .password("password123")
                .phone("1234567890")
                .role("PASSENGER")
                .build();

        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testLoginSuccess() {
        LoginRequest request = LoginRequest.builder()
                .email("john@example.com")
                .password("password123")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(sampleUser));
        when(userDetailsService.loadUserByUsername("john@example.com")).thenReturn(sampleUserDetails);
        when(jwtService.generateToken(sampleUserDetails)).thenReturn("mock.jwt.token");
        when(userMapper.toResponse(sampleUser)).thenReturn(sampleUserResponse);

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("mock.jwt.token", response.getToken());
        assertEquals("john@example.com", response.getUser().getEmail());
    }

    @Test
    void testLoginBadCredentials() {
        LoginRequest request = LoginRequest.builder()
                .email("john@example.com")
                .password("wrongpassword")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
    }
}
