package com.Japkutija.veterinarybackend.veterinary.exception;

import com.Japkutija.veterinarybackend.veterinary.model.entity.Owner;
import lombok.Getter;

@Getter
public class EntitySavingException extends RuntimeException {

    private final Class<?> entityClass;
    private final Exception exception;
    private final String message;
    private String field;

    public EntitySavingException(String message) {
        super(message);
        this.exception = null;
        this.entityClass = null;
        this.message = message;
    }

    public EntitySavingException(Class<?> entityClass, Exception ex) {
        super(String.format("Failed to save: %s", entityClass.getSimpleName()));
        this.exception = ex;
        this.entityClass = entityClass;
        this.message = null;
    }

    public EntitySavingException(String message, Class<?> entityClass, Exception ex) {
        super(message, ex);
        this.entityClass = entityClass;
        this.exception = ex;
        this.message = message;
    }

    public EntitySavingException(String message, String field) {
        super(message);
        this.exception = null;
        this.entityClass = null;
        this.message = message;
        this.field = field;
    }
}
