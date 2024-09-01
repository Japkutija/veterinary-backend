package com.Japkutija.veterinarybackend.veterinary.controller;

import com.Japkutija.veterinarybackend.veterinary.exception.RefreshTokenExpiredException;
import com.Japkutija.veterinarybackend.veterinary.exception.RefreshTokenNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.mapper.UserMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.request.AuthenticationRequest;
import com.Japkutija.veterinarybackend.veterinary.model.dto.request.RefreshTokenRequest;
import com.Japkutija.veterinarybackend.veterinary.model.dto.request.UserRegistrationDto;
import com.Japkutija.veterinarybackend.veterinary.model.dto.response.ApiResponseDto;
import com.Japkutija.veterinarybackend.veterinary.model.dto.response.AuthenticationResponse;
import com.Japkutija.veterinarybackend.veterinary.service.RefreshTokenService;
import com.Japkutija.veterinarybackend.veterinary.service.impl.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Authentication API", description = "Operations for user authentication")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServiceImpl authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user")
    @Tag(name = "Authentication API")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        var response = authService.registerUser(registrationDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Handles the login request and generates a JWT token upon successful authentication.
     *
     * @param authRequest the authentication request containing username and password
     * @return a ResponseEntity containing the authentication response with the JWT token
     */
    @Operation(summary = "Login a user", description = "Logs in a user")
    @Tag(name = "Authentication API")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(
            @RequestBody AuthenticationRequest authRequest,
            HttpServletResponse response) {
        var authResponse = authService.loginUser(authRequest.getUsername(), authRequest.getPassword(), response);

        return ResponseEntity.ok(authResponse);
    }

    @Operation(summary = "Logout a user", description = "Logs out a user")
    @Tag(name = "Authentication API")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.ok(new ApiResponseDto("User logged out successfully"));
    }

    @Operation(summary = "Refresh the access token", description = "Refreshes the access token")
    @Tag(name = "Authentication API")
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshAccessToken(@RequestBody HttpServletRequest request, HttpServletResponse response)
            throws RefreshTokenExpiredException, RefreshTokenNotFoundException {
        var refreshToken = authService.extractRefreshToken(request);
        var authResponse = authService.refreshAccessToken(refreshToken, response);

        return ResponseEntity.ok(authResponse);
    }
}
