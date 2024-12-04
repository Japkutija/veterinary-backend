package com.Japkutija.veterinarybackend.veterinary.service;

import com.Japkutija.veterinarybackend.veterinary.model.dto.request.UserRegistrationDto;
import com.Japkutija.veterinarybackend.veterinary.model.dto.response.AuthenticationResponse;
import com.Japkutija.veterinarybackend.veterinary.model.entity.RefreshToken;
import com.Japkutija.veterinarybackend.veterinary.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

public interface AuthService {

    void logout(HttpServletRequest request, HttpServletResponse response);
    RefreshToken createRefreshToken(String refreshToken, User userEntity);
    AuthenticationResponse refreshAccessToken(String refreshToken, HttpServletResponse response);
    String extractRefreshToken(HttpServletRequest request);
    AuthenticationResponse loginUser(String username, String password, HttpServletResponse response);
    AuthenticationResponse registerUser(UserRegistrationDto registrationDto, HttpServletResponse response);
    void setRefreshTokenCookie(HttpServletResponse response, String refreshToken);

}
