package com.Japkutija.veterinarybackend.veterinary.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents the response containing the JWT token.
 */
@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private String jwt;
    private String refreshToken;
}
