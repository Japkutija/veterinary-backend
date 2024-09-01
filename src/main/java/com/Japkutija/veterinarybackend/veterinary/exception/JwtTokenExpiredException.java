package com.Japkutija.veterinarybackend.veterinary.exception;

public class JwtTokenExpiredException extends RuntimeException {
    public JwtTokenExpiredException(String jwtTokenHasExpired) {
        super(jwtTokenHasExpired);
    }
}
