package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.mapper.UserMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.request.UserRegistrationDto;
import com.Japkutija.veterinarybackend.veterinary.model.dto.response.AuthenticationResponse;
import com.Japkutija.veterinarybackend.veterinary.model.dto.response.ErrorResponseDto;
import com.Japkutija.veterinarybackend.veterinary.model.entity.RefreshToken;
import com.Japkutija.veterinarybackend.veterinary.security.util.JwtUtil;
import com.Japkutija.veterinarybackend.veterinary.service.RefreshTokenService;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserServiceImpl userService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;

    public Object registerUser(UserRegistrationDto registrationDto) {

        // Validate that the passwords match
        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            return ErrorResponseDto.builder()
                    .timestamp(LocalDateTime.now())
                    .message("Passwords do not match")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
        }

        // Register the user
        var registeredUser = userService.registerUser(
                registrationDto.getUsername(),
                registrationDto.getEmail(),
                registrationDto.getPassword()
        );

        return userMapper.toUserDto(registeredUser);
    }

    public AuthenticationResponse loginUser(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        var userDetails = customUserDetailsService.loadUserByUsername(username);
        var userEntity = userService.findByUsername(username);
        var jwt = jwtUtil.generateToken(userDetails.getUsername());
        var refreshToken = UUID.randomUUID().toString();

        var refreshTokenEntity = RefreshToken.builder()
                .token(refreshToken)
                .user(userEntity)
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .build();

        refreshTokenService.saveRefreshToken(refreshTokenEntity);

        return new AuthenticationResponse(jwt, refreshToken);
    }

    public AuthenticationResponse refreshAccessToken(String refreshToken) {
        var refreshTokenEntity = refreshTokenService.findByToken(refreshToken);

        // Check if the refresh token is expired
        if (refreshTokenEntity.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired. Please login again.");
        }

        // Generate a new access token
        var newAccessToken = jwtUtil.generateToken(refreshTokenEntity.getUser().getUsername());

        return new AuthenticationResponse(newAccessToken, refreshToken);
    }
}
