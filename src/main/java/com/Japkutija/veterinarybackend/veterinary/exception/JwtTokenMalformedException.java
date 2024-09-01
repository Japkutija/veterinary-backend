package com.Japkutija.veterinarybackend.veterinary.exception;

public class JwtTokenMalformedException extends RuntimeException {
    public JwtTokenMalformedException(String jwtTokenIsMalformed) {
        super(jwtTokenIsMalformed);
    }
}
