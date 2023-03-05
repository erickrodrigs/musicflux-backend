package com.erickrodrigues.musicflux.handlers;

import com.erickrodrigues.musicflux.exceptions.InvalidCredentialsException;
import com.erickrodrigues.musicflux.exceptions.ResourceAlreadyExistsException;
import com.erickrodrigues.musicflux.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ ResourceNotFoundException.class })
    public ResponseEntity<ApiError> resourceNotFoundHandler(ResourceNotFoundException exception,
                                                            HttpServletRequest request) {
        return new ResponseEntity<>(
                ApiError.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.NOT_FOUND.value())
                        .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .message(exception.getMessage())
                        .path(request.getRequestURI())
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler({ ResourceAlreadyExistsException.class })
    public ResponseEntity<ApiError> resourceAlreadyExistsHandler(ResourceAlreadyExistsException exception,
                                                                 HttpServletRequest request) {
        return new ResponseEntity<>(
                ApiError.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .message(exception.getMessage())
                        .path(request.getRequestURI())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({ InvalidCredentialsException.class })
    public ResponseEntity<ApiError> invalidCredentialsHandler(InvalidCredentialsException exception,
                                                              HttpServletRequest request) {
        return new ResponseEntity<>(
                ApiError.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                        .message(exception.getMessage())
                        .path(request.getRequestURI())
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler({ RuntimeException.class })
    public ResponseEntity<ApiError> internalErrorHandler(RuntimeException exception,
                                                         HttpServletRequest request) {
        return new ResponseEntity<>(
                ApiError.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .message(exception.getMessage())
                        .path(request.getRequestURI())
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
