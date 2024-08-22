package com.Japkutija.veterinarybackend.veterinary.controller;

import com.Japkutija.veterinarybackend.veterinary.mapper.UserMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.request.AuthenticationRequest;
import com.Japkutija.veterinarybackend.veterinary.model.dto.request.UserRegistrationDto;
import com.Japkutija.veterinarybackend.veterinary.model.dto.response.AuthenticationResponse;
import com.Japkutija.veterinarybackend.veterinary.model.dto.response.ErrorResponseDto;
import com.Japkutija.veterinarybackend.veterinary.security.CustomUserDetailsService;
import com.Japkutija.veterinarybackend.veterinary.security.util.JwtUtil;
import com.Japkutija.veterinarybackend.veterinary.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserServiceImpl userService;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            var errorResponseDto = ErrorResponseDto.builder()
                    .timestamp(LocalDateTime.now())
                    .message("Passwords do not match")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
            return ResponseEntity.badRequest().body(errorResponseDto);
        }

        var registeredUser = userService.registerUser(
                registrationDto.getUsername(),
                registrationDto.getEmail(),
                registrationDto.getPassword()
        );

        var registeredUserDto = userMapper.toUserDto(registeredUser);

        return ResponseEntity.ok(registeredUserDto);
    }

    /**
     * Handles the login request and generates a JWT token upon successful authentication.
     *
     * @param authRequest the authentication request containing username and password
     * @return a ResponseEntity containing the authentication response with the JWT token
     * @throws Exception if authentication fails
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authRequest) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        final var userDetails = customUserDetailsService.loadUserByUsername(authRequest.getUsername());
        final var jwt = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
