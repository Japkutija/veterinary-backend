package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.config.CookieConfig;
import com.Japkutija.veterinarybackend.veterinary.exception.BadRequestException;
import com.Japkutija.veterinarybackend.veterinary.exception.ErrorResponseException;
import com.Japkutija.veterinarybackend.veterinary.mapper.UserMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.request.UserRegistrationDto;
import com.Japkutija.veterinarybackend.veterinary.model.dto.response.AuthenticationResponse;
import com.Japkutija.veterinarybackend.veterinary.model.entity.User;
import com.Japkutija.veterinarybackend.veterinary.model.enums.Role;
import com.Japkutija.veterinarybackend.veterinary.security.util.JwtUtil;
import com.Japkutija.veterinarybackend.veterinary.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private CookieConfig cookieConfig;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private ApplicationContext applicationContext;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Authentication authentication;

    @Spy
    @InjectMocks
    private AuthServiceImpl authService;

    private UserRegistrationDto validRegistrationDto;
    private User registeredUser;

    @BeforeEach
    void setUp() {
        validRegistrationDto = UserRegistrationDto.builder()
                .username("testuser")
                .email("test@example.com")
                .password("Password123!")
                .confirmPassword("Password123!")
                .firstName("Test")
                .lastName("User")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .emso("1234567890123")
                .phoneNumber("+38612345678")
                .address("Test Address 123")
                .build();

        registeredUser = User.builder()
                .username("testuser")
                .email("test@example.com")
                .role(Role.USER)
                .build();
    }

    @Test
    void registerUser_Success() {
        when(userService.registerUser(
                validRegistrationDto.getUsername(),
                validRegistrationDto.getEmail(),
                validRegistrationDto.getPassword(),
                validRegistrationDto.getFirstName(),
                validRegistrationDto.getLastName(),
                validRegistrationDto.getDateOfBirth(),
                validRegistrationDto.getEmso(),
                validRegistrationDto.getPhoneNumber(),
                validRegistrationDto.getAddress()
        )).thenReturn(registeredUser);

        // Mock the application context to return the AuthServiceImpl instance
        when(applicationContext.getBean(AuthServiceImpl.class)).thenReturn(authService);

        var authResponse = new AuthenticationResponse("test.jwt.token");

        doReturn(authResponse).when(authService).loginUser(anyString(), anyString(), any(HttpServletResponse.class));

        // Act
        var result = authService.registerUser(validRegistrationDto, response);

        // Assert
        assertNotNull(result);
        assertEquals(authResponse.getJwt(), result.getJwt());
    }

    @Test
    void registerUser_WithMismatchedPasswords_ThrowsErrorResponseException() {
        // Arrange
        validRegistrationDto.setConfirmPassword("DifferentPassword123!");

        // Act & Assert
        assertThrows(
                ErrorResponseException.class,
                () -> authService.registerUser(validRegistrationDto, response));
    }

    @Test
    void loginUser_WithValidCredentials_ReturnsAuthenticationResponse() throws Exception {
        // Arrange
        var username = "testuser";
        var password = "password123";
        var jwtToken = "test.jwt.token";
        var user = User.builder()
                .username(username)
                .role(Role.USER)
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(username, password));


        when(userService.findByUsername(username)).thenReturn(user);

        when(jwtUtil.generateToken(username, user.getRole())).thenReturn(jwtToken);

        // Act
        var result = authService.loginUser(username, password, response);

        // Assert
        assertEquals(jwtToken, result.getJwt());
        verify(response).addHeader(eq("Set-Cookie"), anyString());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Clean up SecurityContextHolder
        SecurityContextHolder.clearContext();

    }

    @Test
    void loginUser_WithInvalidCredentials_ThrowsBadRequestException() {
        // Arrange
        var username = "testuser";
        var password = "wrongpassword";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(BadCredentialsException.class);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            authService.loginUser(username, password, response);
        });

        // Verify that the response was not added to the cookie
        verify(response, never()).addHeader(anyString(), anyString());


        // Clean up SecurityContextHolder
        SecurityContextHolder.clearContext();
    }


   /* @Test
    void loginUser_Success() {
        // Arrange
        String username = "testuser";
        String password = "Password123!";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userService.findByUsername(username)).thenReturn(registeredUser);
        when(jwtUtil.generateToken(username, registeredUser.getRole())).thenReturn("test.jwt.token");

        // Act
        AuthenticationResponse result = authService.loginUser(username, password, response);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).findByUsername(username);
    }

    @Test
    void loginUser_InvalidCredentials() {
        // Arrange
        String username = "testuser";
        String password = "WrongPassword123!";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // Act & Assert
        assertThrows(
                BadRequestException.class,
                () -> authService.loginUser(username, password, response));
    }*/
}