package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.exception.BadRequestException;
import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.exception.RefreshTokenExpiredException;
import com.Japkutija.veterinarybackend.veterinary.exception.RefreshTokenNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.mapper.UserMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.request.UserRegistrationDto;
import com.Japkutija.veterinarybackend.veterinary.model.dto.response.AuthenticationResponse;
import com.Japkutija.veterinarybackend.veterinary.model.dto.response.ErrorResponseDto;
import com.Japkutija.veterinarybackend.veterinary.model.entity.RefreshToken;
import com.Japkutija.veterinarybackend.veterinary.model.entity.User;
import com.Japkutija.veterinarybackend.veterinary.security.util.JwtUtil;
import com.Japkutija.veterinarybackend.veterinary.service.RefreshTokenService;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserServiceImpl userService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;

    @Value("${jwt.refresh-token-expiration}") //JWT_REFRESH_TOKEN_EXPIRATION
    private int refreshTokenExpirationInDays;

    public Object registerUser(UserRegistrationDto registrationDto) {

        // Validate that the passwords match
        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            log.error("User registration failed: Passwords do not match");
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

    @Transactional
    public AuthenticationResponse loginUser(String username, String password) {
        try {
            // Attempt to authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // Load user details from the database
            var userDetails = customUserDetailsService.loadUserByUsername(username);
            var userEntity = userService.findByUsername(username);

            // Generate JWT and refresh token
            var jwt = jwtUtil.generateToken(userDetails.getUsername());
            var refreshToken = UUID.randomUUID().toString();
            var refreshTokenEntity = createRefreshToken(refreshToken, userEntity);

            refreshTokenService.saveRefreshToken(refreshTokenEntity);

            log.info("Login successful for user {}", username);
            return new AuthenticationResponse(jwt, refreshToken);
        } catch (BadCredentialsException ex) {
            log.error("Login failed: Incorrect credentials for user {}", username);
            throw new BadRequestException("Incorrect username or password");
        } catch (UsernameNotFoundException ex) {
            log.error("Login failed: User {} not found", username);
            throw new EntityNotFoundException(User.class, username);
        } catch (Exception ex) {
            log.error("Login failed: An unexpected error occurred during login", ex);
            throw new RuntimeException("Login failed: An unexpected error occurred");
        }
    }

    private RefreshToken createRefreshToken(String refreshToken, User userEntity) {
        return RefreshToken.builder()
                .token(refreshToken)
                .user(userEntity)
                .expiryDate(Instant.now().plus(refreshTokenExpirationInDays, ChronoUnit.DAYS))
                .build();
    }

    public AuthenticationResponse refreshAccessToken(String refreshToken) {
        try {
            // Look up the refresh token entity
            var refreshTokenEntity = refreshTokenService.findByToken(refreshToken);

            // Check if the refresh token is expired
            if (refreshTokenEntity.getExpiryDate().isBefore(Instant.now())) {
                log.error("Refresh token expired for user `{}`", refreshTokenEntity.getUser().getUsername());
                throw new RefreshTokenExpiredException("Refresh token expired. Please login again.");
            }

            // Invalidate the old refreshed token by deleting it from the database
            refreshTokenService.deleteByToken(refreshToken);

            // Generate a new access token and refresh token
            var newAccessToken = jwtUtil.generateToken(refreshTokenEntity.getUser().getUsername());
            var newRefreshToken = UUID.randomUUID().toString();

            // Save the new refresh token in the database
            var newRefreshTokenEntity = RefreshToken.builder()
                    .token(newRefreshToken)
                    .user(refreshTokenEntity.getUser())
                    .expiryDate(Instant.now().plus(refreshTokenExpirationInDays, ChronoUnit.DAYS))
                    .build();

            refreshTokenService.saveRefreshToken(newRefreshTokenEntity);

            // Return the new access token and refresh token
            return new AuthenticationResponse(newAccessToken, newRefreshToken);
        } catch (RefreshTokenExpiredException ex) {
            log.error("Refresh token expired: `{}`", refreshToken);
            throw ex;
        } catch (Exception ex) {
            log.error("Failed to refresh access token: {}", ex.getMessage());
            throw new RefreshTokenNotFoundException("Refresh token not found");
        }
    }
}
