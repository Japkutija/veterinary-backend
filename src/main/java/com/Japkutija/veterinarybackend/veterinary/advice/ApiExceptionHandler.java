package com.Japkutija.veterinarybackend.veterinary.advice;

import com.Japkutija.veterinarybackend.veterinary.exception.BadRequestException;
import com.Japkutija.veterinarybackend.veterinary.exception.EntityDeletionException;
import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.exception.EntitySavingException;
import com.Japkutija.veterinarybackend.veterinary.exception.JwtTokenExpiredException;
import com.Japkutija.veterinarybackend.veterinary.exception.JwtTokenMalformedException;
import com.Japkutija.veterinarybackend.veterinary.exception.RefreshTokenExpiredException;
import com.Japkutija.veterinarybackend.veterinary.exception.UserAlreadyExistsException;
import com.Japkutija.veterinarybackend.veterinary.exception.ValidationException;
import com.Japkutija.veterinarybackend.veterinary.model.dto.response.ValidationErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler(Exception.class)
    public void handleException(Exception ex, HttpServletRequest request) {
        log.error("Exception occurred: ", ex);
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {

        var errorMessage = "Access denied";
        var response = new ApiErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                errorMessage,
                ex.getMessage(),
                request.getRequestURI());

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EntityDeletionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiErrorResponse> handleEntityDeletionException(EntityDeletionException ex, HttpServletRequest request) {
        var response = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error occurred while deleting entity",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JwtTokenMalformedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleJwtTokenMalformedException(JwtTokenMalformedException ex, HttpServletRequest request) {
        var response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Malformed JWT Token",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JwtTokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiErrorResponse> handleJwtTokenExpiredException(JwtTokenExpiredException ex, HttpServletRequest request) {
        var response = new ApiErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Expired JWT Token",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiErrorResponse> handleRunTimeException(RuntimeException ex, HttpServletRequest request) {

        var response = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // Return 404 status code
    public ResponseEntity<ApiErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex, HttpServletRequest request) {
        log.error("User not found: {}", ex.getMessage());

        var response = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "User not found",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ValidationErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex, HttpServletRequest request) {
        log.error("Error: User already exists:", ex);

        List<ValidationErrorResponse.FieldError> fieldErrors = new ArrayList<>();

        if (ex.getField() == null) {
            fieldErrors.add(new ValidationErrorResponse.FieldError("general", ex.getMessage()));
        } else {
            fieldErrors.add(new ValidationErrorResponse.FieldError(ex.getField(), ex.getMessage()));
        }

        var errorMessage = "Error: " + ex.getMessage();
        var response = new ValidationErrorResponse(
                HttpStatus.CONFLICT.value(),
                errorMessage,
                ex.getMessage(),
                request.getRequestURI(),
                fieldErrors);

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {

        log.info("Resource not found for request: {}", request.getRequestURI(), ex); // Centralized logging;
        var errorMessage = "Resource not found";
        var response = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                errorMessage,
                ex.getMessage(),
                request.getRequestURI());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiErrorResponse> handleRefreshTokenExpiredException(RefreshTokenExpiredException ex, HttpServletRequest request) {
        var response = new ApiErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized - Refresh Token Expired",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(BadRequestException ex, HttpServletRequest request) {
        log.error("Bad request: ", ex);

        String errorMessage = "The request was not valid.";
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                errorMessage,
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntitySavingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleEntitySavingException(EntitySavingException ex, HttpServletRequest request) {
        log.error("Entity saving exception: ", ex);

        var response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad request",
                ex.getMessage(),
                request.getRequestURI());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        log.error("Validation error: ", ex);

        var fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ValidationErrorResponse.FieldError(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        var response = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                "Validation failed for one or more fields",
                request.getRequestURI(),
                fieldErrors
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(
            ValidationException ex, HttpServletRequest request) {

        var response = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                ex.getMessage(),
                request.getRequestURI(),
                ex.getFieldErrors()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
