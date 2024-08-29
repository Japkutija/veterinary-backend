package com.Japkutija.veterinarybackend.veterinary.exception;


import lombok.Getter;


@Getter
public class GeneralRunTimeException extends RuntimeException {

    public GeneralRunTimeException(String message) {
        super(message);
    }

    public GeneralRunTimeException(String message, Exception exception) {
        super(message, exception);
    }

    public GeneralRunTimeException(Class<?> entityClass) {
        super(String.format("Bad request: %s", entityClass.getSimpleName()));
    }

    public GeneralRunTimeException(String failedToSaveOwner, Class<?> entityClass, Exception ex) {
        super(String.format("Failed to save: %s", entityClass.getSimpleName()), ex);
    }
}
