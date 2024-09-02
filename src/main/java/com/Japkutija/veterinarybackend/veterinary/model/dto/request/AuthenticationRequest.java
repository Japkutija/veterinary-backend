package com.Japkutija.veterinarybackend.veterinary.model.dto.request;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}