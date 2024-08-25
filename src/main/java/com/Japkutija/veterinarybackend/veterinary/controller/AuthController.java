package com.Japkutija.veterinarybackend.veterinary.controller;

import com.Japkutija.veterinarybackend.veterinary.mapper.UserMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.request.AuthenticationRequest;
import com.Japkutija.veterinarybackend.veterinary.model.dto.request.RefreshTokenRequest;
import com.Japkutija.veterinarybackend.veterinary.model.dto.request.UserRegistrationDto;
import com.Japkutija.veterinarybackend.veterinary.model.dto.response.AuthenticationResponse;
import com.Japkutija.veterinarybackend.veterinary.service.impl.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
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
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authRequest) {
        var response = authService.loginUser(authRequest.getUsername(), authRequest.getPassword());

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to refresh the access token using a provided refresh token.
     *
     * @param refreshTokenRequest the request containing the refresh token
     * @return a ResponseEntity containing the new authentication response with the refreshed access token
     */
    @Operation(summary = "Refresh the access token", description = "Refreshes the access token")
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        var response = authService.refreshAccessToken(refreshTokenRequest.getRefreshToken());

        return ResponseEntity.ok(response);
    }
}
