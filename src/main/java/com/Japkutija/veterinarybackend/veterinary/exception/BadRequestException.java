package com.Japkutija.veterinarybackend.veterinary.exception;


import com.Japkutija.veterinarybackend.veterinary.model.entity.Owner;
import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Exception exception) {
        super(message, exception);
    }

    public BadRequestException(Class<?> entityClass) {
        super(String.format("Bad request: %s", entityClass.getSimpleName()));
    }

    public BadRequestException(String failedToSaveOwner, Class<?> entityClass, Exception ex) {
        super(String.format("Failed to save: %s", entityClass.getSimpleName()), ex);
    }
}
