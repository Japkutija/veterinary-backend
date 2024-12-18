package com.Japkutija.veterinarybackend.veterinary.exception;

import com.Japkutija.veterinarybackend.veterinary.model.entity.Owner;
import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {

    private Class<Owner> ownerClass;
    private String field;


    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String message, Exception exception) {
        super(message, exception);
    }

    public UserAlreadyExistsException(Class<?> entityClass, Object identifier) {
        super(String.format("User with %s %s already exists", entityClass.getSimpleName(), identifier));
    }

    public UserAlreadyExistsException(String message, Class<Owner> ownerClass, Exception ex) {
        super(message, ex);
        this.ownerClass = ownerClass;

    }

    public UserAlreadyExistsException(String message, String field) {
        super(message);
        this.field = field;
    }
}
