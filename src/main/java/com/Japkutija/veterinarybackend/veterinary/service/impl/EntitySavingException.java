package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.model.entity.Owner;
import lombok.Getter;

@Getter
public class EntitySavingException extends RuntimeException {

    public EntitySavingException(String message) {
        super(message);
    }

    public EntitySavingException(Class<?> entityClass, Exception ex) {
        super(String.format("Failed to save: %s", entityClass.getSimpleName()));
    }

    public EntitySavingException(String message, Class<?> entityClass, Exception ex) {
        super(String.format("Failed to save: %s", entityClass.getSimpleName()), ex);
    }
}
