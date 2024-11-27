package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.config.CookieConfig;
import com.Japkutija.veterinarybackend.veterinary.exception.BadRequestException;
import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.exception.GeneralRunTimeException;
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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl {

    private final AuthenticationManager authenticationManager;
    private final CookieConfig cookieConfig;
    private final UserServiceImpl userService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    public static final String REFRESH_TOKEN_NOT_FOUND = "Refresh token not found in cookies";

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
                registrationDto.getPassword(),
                registrationDto.getFirstName(),
                registrationDto.getLastName(),
                registrationDto.getDateOfBirth(),
                registrationDto.getEmso(),
                registrationDto.getPhoneNumber(),
                registrationDto.getAddress()
        );

        return userMapper.toUserDto(registeredUser);
    }

    @Transactional
    public AuthenticationResponse loginUser(String username, String password, HttpServletResponse response) {
        try {
            // Attempt to authenticate the user. Automatically invokes CustomUserDetailsService.loadUserByUsername to load user details
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // Set the Authentication object in SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Load user details from the database
            // No need to load User Details here, as the user is already authenticated!
            // Still need to retrieve the user entity to create the refresh token!
            var userEntity = userService.findByUsername(username);

            // Generate JWT and refresh token
            var jwt = jwtUtil.generateToken(username, userEntity.getRole());
            var refreshToken = UUID.randomUUID().toString();
            var refreshTokenEntity = createRefreshToken(refreshToken, userEntity);

            refreshTokenService.saveRefreshToken(refreshTokenEntity);

            // Set the refresh token as HttpOnly cookie
            setRefreshTokenCookie(response, refreshToken);

            log.info("Login successful for user {}", username);
            return new AuthenticationResponse(jwt);
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

    public void logout(HttpServletRequest request, HttpServletResponse response) {

        var refreshToken = Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        if (refreshToken == null) {
            log.error(REFRESH_TOKEN_NOT_FOUND);
            throw new GeneralRunTimeException("Refresh token is missing");
        }

        log.info("Deleting refresh token: {}", refreshToken);
        refreshTokenService.deleteByToken(refreshToken);

        var cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(cookieConfig.isSecure())
                .sameSite(cookieConfig.getSameSite())
                .path("/api/auth/")
                .maxAge(0) // expires immediately
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        log.info("User logged out successfully and refresh token deleted");
    }

    /**
     * Sets the refresh token as an HttpOnly cookie in the response.
     *
     * @param response     the HttpServletResponse to which the cookie will be added
     * @param refreshToken the refresh token to be set as a cookie
     */
    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        var refreshTokenCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                .httpOnly(true)
                .secure(cookieConfig.isSecure())
                .path("/api/auth/") // Scope the cookie to this path, so it's not sent with every request.
                .maxAge(Duration.ofDays(refreshTokenExpirationInDays)) // Set expiration time
                .sameSite(cookieConfig.getSameSite()) //
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
            var newAccessToken = jwtUtil.generateToken(refreshTokenEntity.getUser().getUsername(), refreshTokenEntity.getUser().getRole());
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
            return new AuthenticationResponse(newAccessToken);
        } catch (RefreshTokenExpiredException ex) {
            log.error("Refresh token expired: `{}`", refreshToken);
            throw ex;
        } catch (Exception ex) {
            log.error("Failed to refresh access token: {}", ex.getMessage());
            throw new RefreshTokenNotFoundException("Refresh token not found");
        }
    }

    /**
     * Extracts the refresh token from the cookies in the HTTP request.
     * <p>
     * This method retrieves the refresh token from the cookies present in the HTTP request.
     * If the cookies are null or the refresh token is not found, it logs an error and throws a BadRequestException.
     *
     * @param request the HttpServletRequest containing the cookies
     * @return the value of the refresh token
     * @throws BadRequestException if the refresh token is not found in the cookies
     */
    public String extractRefreshToken(HttpServletRequest request) {

        if (request.getCookies() == null) {
            log.error(REFRESH_TOKEN_NOT_FOUND);
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new BadRequestException(REFRESH_TOKEN_NOT_FOUND));
    }
}
