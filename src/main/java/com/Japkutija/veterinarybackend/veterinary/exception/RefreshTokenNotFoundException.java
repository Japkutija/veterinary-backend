package com.Japkutija.veterinarybackend.veterinary.exception;

public class RefreshTokenNotFoundException extends RuntimeException {
    public RefreshTokenNotFoundException(String refreshTokenNotFound) {
        super(refreshTokenNotFound);
    }
}
