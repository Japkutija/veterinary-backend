package com.Japkutija.veterinarybackend.veterinary.controller;

import com.Japkutija.veterinarybackend.veterinary.exception.RefreshTokenExpiredException;
import com.Japkutija.veterinarybackend.veterinary.exception.RefreshTokenNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.mapper.UserMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.UserDto;
import com.Japkutija.veterinarybackend.veterinary.model.dto.request.AuthenticationRequest;
import com.Japkutija.veterinarybackend.veterinary.model.dto.request.UserRegistrationDto;
import com.Japkutija.veterinarybackend.veterinary.model.dto.response.ApiResponseDto;
import com.Japkutija.veterinarybackend.veterinary.model.dto.response.AuthenticationResponse;
import com.Japkutija.veterinarybackend.veterinary.service.RefreshTokenService;
import com.Japkutija.veterinarybackend.veterinary.service.impl.AuthServiceImpl;
import com.Japkutija.veterinarybackend.veterinary.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "User API", description = "Operations for user management")
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final UserMapper userMapper;

    @Operation(summary = "Get user profile", description = "Retrieve the authenticated user's profile")
    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDto> getProfile(Authentication authentication) {
        var username = authentication.getName();
        var user = userService.findByUsername(username);
        var userDto = userMapper.toUserDto(user);
        return ResponseEntity.ok(userDto);
    }
}
