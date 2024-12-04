package com.Japkutija.veterinarybackend.veterinary.exception;

import com.Japkutija.veterinarybackend.veterinary.model.dto.response.ValidationErrorResponse;
import com.Japkutija.veterinarybackend.veterinary.model.dto.response.ValidationErrorResponse.FieldError;
import java.util.List;
import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
    private final List<ValidationErrorResponse.FieldError> fieldErrors;

    public ValidationException(List<FieldError> fieldErrors) {
        super("Validation failed for one or more fields");
        this.fieldErrors = fieldErrors;
    }
}