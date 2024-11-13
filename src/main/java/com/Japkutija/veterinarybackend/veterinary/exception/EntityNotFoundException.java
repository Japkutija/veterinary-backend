package com.Japkutija.veterinarybackend.veterinary.exception;

import com.Japkutija.veterinarybackend.veterinary.model.entity.Owner;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Exception exception) {
        super(message, exception);
    }

    public EntityNotFoundException(Class<?> entityClass, Object identifier) {
        super(String.format("Entity '%s' with identifier '%s' not found", entityClass.getSimpleName(), identifier.toString()));
    }
}
