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
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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
    public AuthenticationResponse loginUser(String username, String password, HttpServletResponse response) {
        try {
            // Attempt to authenticate the user. Automatically invokes CustomUserDetailsService.loadUserByUsername to load user details
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // Load user details from the database
            // No need to load user details here, as the user is already authenticated!
            // Still need to retrieve the user entity to create the refresh token!
            //var userDetails = customUserDetailsService.loadUserByUsername(username);

            var userEntity = userService.findByUsername(username);

            // Generate JWT and refresh token
            var jwt = jwtUtil.generateToken(username);
            var refreshToken = UUID.randomUUID().toString();
            var refreshTokenEntity = createRefreshToken(refreshToken, userEntity);

            refreshTokenService.saveRefreshToken(refreshTokenEntity);

            // Set the refresh token as HttpOnly cookie
            setRefreshTokenCookie(response, refreshToken);

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

    /**
     * Sets the refresh token as an HttpOnly cookie in the response.
     *
     * @param response     the HttpServletResponse to which the cookie will be added
     * @param refreshToken the refresh token to be set as a cookie
     */
    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        var refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true) // Only send over HTTPS
                .path("/api/auth/refresh-token") // Scope the cookie to this path, so it's not sent with every request.
                .maxAge(Duration.ofDays(refreshTokenExpirationInDays)) // Set expiration time
                .sameSite("Strict") // Prevent CSRF attacks
                .build();

        // Add the cookie to the response header
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }

    private RefreshToken createRefreshToken(String refreshToken, User userEntity) {
        return RefreshToken.builder()
                .token(refreshToken)
                .user(userEntity)
                .expiryDate(Instant.now().plus(refreshTokenExpirationInDays, ChronoUnit.DAYS))
                .build();
    }

    public AuthenticationResponse refreshAccessToken(String refreshToken, HttpServletResponse response) {
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

            setRefreshTokenCookie(response, newRefreshToken);

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
