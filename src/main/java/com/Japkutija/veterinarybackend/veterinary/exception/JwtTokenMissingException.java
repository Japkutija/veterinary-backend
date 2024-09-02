package com.Japkutija.veterinarybackend.veterinary.exception;

public class JwtTokenMissingException extends RuntimeException {
    public JwtTokenMissingException(String s) {
        super(s);
    }
}
