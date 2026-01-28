package com.training.handson.controllers;

import com.commercetools.api.client.error.BadRequestException;
import com.training.handson.dto.ErrorResponse;
import io.vrap.rmf.base.client.error.*;
import io.vrap.rmf.base.client.oauth2.AuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler(ApiServerException.class)
    public ResponseEntity<ApiServerException> handleApiServerException(ApiServerException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "INTERNAL_SERVER_ERROR"
        );
        return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestException> handleBadRequestException(BadRequestException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "BAD_REQUEST_ERROR"
        );
        return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConcurrentModificationException.class)
    public ResponseEntity<ConcurrentModificationException> handleConcurrentModificationException(ConcurrentModificationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "CONFLICT_ERROR"
        );
        return new ResponseEntity<>(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ForbiddenException> handleForbiddenException(ForbiddenException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "FORBIDDEN_ERROR"
        );
        return new ResponseEntity<>(ex, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<UnauthorizedException> handleUnauthorizedException(UnauthorizedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "UNAUTHORIZED_ERROR"
        );
        return new ResponseEntity<>(ex, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<NotFoundException> handleNotFoundException(NotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "NOT_FOUND_ERROR"
        );
        return new ResponseEntity<>(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<AuthException> handleAuthException(AuthException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "NETWORK_AUTHENTICATION_REQUIRED_ERROR"
        );
        return new ResponseEntity<>(ex, HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
    }

    @ExceptionHandler(HttpMessageNotWritableException.class)
    public ResponseEntity<HttpMessageNotWritableException> handleHttpMessageNotWritableException(HttpMessageNotWritableException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "NOT_IMPLEMENTED_ERROR"
        );
        return new ResponseEntity<>(ex, HttpStatus.NOT_IMPLEMENTED);
    }

}
