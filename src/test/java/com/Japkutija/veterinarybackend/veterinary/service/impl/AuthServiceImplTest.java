package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.config.CookieConfig;
import com.Japkutija.veterinarybackend.veterinary.exception.BadRequestException;
import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.exception.ErrorResponseException;
import com.Japkutija.veterinarybackend.veterinary.exception.GeneralRunTimeException;
import com.Japkutija.veterinarybackend.veterinary.exception.RefreshTokenExpiredException;
import com.Japkutija.veterinarybackend.veterinary.mapper.UserMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.request.UserRegistrationDto;
import com.Japkutija.veterinarybackend.veterinary.model.dto.response.AuthenticationResponse;
import com.Japkutija.veterinarybackend.veterinary.model.entity.RefreshToken;
import com.Japkutija.veterinarybackend.veterinary.model.entity.User;
import com.Japkutija.veterinarybackend.veterinary.model.enums.Role;
import com.Japkutija.veterinarybackend.veterinary.security.util.JwtUtil;
import com.Japkutija.veterinarybackend.veterinary.service.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static com.Japkutija.veterinarybackend.veterinary.service.impl.AuthServiceImpl.REFRESH_TOKEN_NOT_FOUND;
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
    private HttpServletRequest request;
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

    @Test
    void loginUser_WithInvalidCredentials_ThrowsEntityNotFoundException() {
        // Arrange
        var username = "testuser";
        var password = "wrongpassword";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(UsernameNotFoundException.class);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            authService.loginUser(username, password, response);
        });

        // Verify that the response was not added to the cookie
        verify(response, never()).addHeader(anyString(), anyString());

        // Werify that we are not returning a token

        verify(jwtUtil, never()).generateToken(anyString(), any(Role.class));

        // Clean up SecurityContextHolder
        SecurityContextHolder.clearContext();
    }

    // Unexpected exception during authentication is wrapped in RuntimeException
    @Test
    void loginUser_WhenUnexpectedExceptionOccurs_ThrowsRuntimeException() {
        // Arrange
        var username = "testUser";
        var password = "password123";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            authService.loginUser(username, password, response);
        });

        verifyNoInteractions(userService, jwtUtil, refreshTokenService);

        SecurityContextHolder.clearContext();
    }

    @Test
    void logout_WithRefreshToken_SuccessfullyLogsOutUser() {
        // Arrange
        setupCookies("refreshToken", "valid-token");

        when(cookieConfig.isSecure()).thenReturn(true);
        when(cookieConfig.getSameSite()).thenReturn("Strict");

        // Act
        assertDoesNotThrow(() -> authService.logout(request, response));

        var cookieHeaderCaptor = ArgumentCaptor.forClass(String.class);

        // Assert
        verify(refreshTokenService).deleteByToken("valid-token");
        verify(response).addHeader(eq("Set-Cookie"), cookieHeaderCaptor.capture());
        assertCookieCleared(cookieHeaderCaptor.getValue());

        // Verify SecurityContextHolder is cleared
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    private void setupCookies(String name, String value) {
        var refreshTokenCookie = new Cookie(name, value);
        var cookies = new Cookie[] {refreshTokenCookie};
        when(request.getCookies()).thenReturn(cookies);
    }

    private void assertCookieCleared(String cookieHeader) {
        assertTrue(cookieHeader.contains("refreshToken="));
        assertTrue(cookieHeader.contains("Max-Age=0"));
        assertTrue(cookieHeader.contains("Path=/api/auth/"));
        assertTrue(cookieHeader.contains("SameSite=Strict"));
        assertTrue(cookieHeader.contains("Secure"));
    }

    @Test
    void logout_WithoutRefreshToken_ThrowsException() {
        // Arrange
        when(request.getCookies()).thenReturn(null);

        // Act & Assert
        var exception = assertThrows(GeneralRunTimeException.class, () -> {
            authService.logout(request, response);
        });

        assertEquals("Refresh token is missing", exception.getMessage());

        // Verify that deleteByToken is not called
        verify(refreshTokenService, never()).deleteByToken(anyString());

        // Verify that no headers are added to the response
        verify(response, never()).addHeader(anyString(), anyString());
    }

    @Test
    void refreshAccessToken_WithValidRefreshToken_ReturnsNewAccessToken() {
        // Arrange
        var refreshTokenValue = "valid-refresh-token";
        var newAccessToken = "new-access-token";

        var user = User.builder()
                .id(1L)
                .username("testuser")
                .role(Role.USER)
                .build();

        var refreshToken = RefreshToken.builder()
                .token(refreshTokenValue)
                .user(user)
                .expiryDate(Instant.now().plusSeconds(3600))
                .build();

        when(refreshTokenService.findByToken(refreshTokenValue)).thenReturn(refreshToken);
        when(jwtUtil.generateToken(user.getUsername(), user.getRole())).thenReturn(newAccessToken);

        // Act
        var authResponse = authService.refreshAccessToken(refreshTokenValue, response);

        // Assert
        assertDoesNotThrow(() -> authResponse);
        assertEquals(newAccessToken, authResponse.getJwt());

        // Verify that the refresh token was deleted
        verify(refreshTokenService).findByToken(refreshTokenValue);
        verify(refreshTokenService).deleteByToken(refreshTokenValue);
        verify(refreshTokenService).saveRefreshToken(any(RefreshToken.class));
        verify(response).addHeader(eq("Set-Cookie"), any(String.class));

        // Verify SecurityContextHolder is cleared
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void extractRefreshToken_NoCookiesInRequest_ThrowsBadRequestException() {
        // Arrange
        when(request.getCookies()).thenReturn(null);

        // Act & Assert
        var exception = assertThrows(BadRequestException.class, () -> {
            authService.extractRefreshToken(request);
        });

        assertEquals(REFRESH_TOKEN_NOT_FOUND, exception.getMessage());
        verify(request).getCookies();
    }

    @Test
    void extractRefreshToken_RefreshTokenCookieMissing_ThrowsBadRequestException() {
        // Arrange
        var someOtherCookie = new Cookie("someOtherCookie", "someValue");
        var cookies = new Cookie[] {someOtherCookie};

        when(request.getCookies()).thenReturn(cookies);

        // Act & Assert
        var exception = assertThrows(BadRequestException.class, () -> {
            authService.extractRefreshToken(request);
        });

        assertEquals("Refresh token not found in cookies", exception.getMessage());
    }

    @Test
    void refreshAccessToken_WithExpiredRefreshToken_ThrowsRefreshTokenExpiredException() {
        // Arrange
        var refreshTokenValue = "expired-refresh-token";
        var newAccessToken = "new-access-token";

        var user = User.builder()
                .id(1L)
                .username("testuser")
                .role(Role.USER)
                .build();

        var refreshToken = RefreshToken.builder()
                .token(refreshTokenValue)
                .user(user)
                .expiryDate(Instant.now().minusSeconds(3600))
                .build();

        when(refreshTokenService.findByToken(refreshTokenValue)).thenReturn(refreshToken);

        // Act & Assert
        assertThrows(RefreshTokenExpiredException.class, () -> {
            authService.refreshAccessToken(refreshTokenValue, response);
        });
    }
}