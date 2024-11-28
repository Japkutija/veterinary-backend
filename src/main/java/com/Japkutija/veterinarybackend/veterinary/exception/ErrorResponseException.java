package com.Japkutija.veterinarybackend.veterinary.exception;


import com.Japkutija.veterinarybackend.veterinary.model.dto.response.ErrorResponseDto;
import lombok.Getter;

@Getter
public class ErrorResponseException extends RuntimeException {
    private ErrorResponseDto errorResponseDto;

    public ErrorResponseException(ErrorResponseDto errorResponseDto) {
        this.errorResponseDto = errorResponseDto;
    }

    public ErrorResponseDto getErrorResponseDto() {
        return errorResponseDto;
    }
}
