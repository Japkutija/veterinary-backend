package com.Japkutija.veterinarybackend.veterinary.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String message, Exception exception) {
        super(message, exception);
    }

    public UserAlreadyExistsException(Class<?> entityClass, Object identifier) {
        super(String.format("User with %s %s already exists", entityClass.getSimpleName(), identifier));
    }
}
