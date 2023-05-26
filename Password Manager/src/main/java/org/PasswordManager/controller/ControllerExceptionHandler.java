package org.PasswordManager.controller;

import lombok.extern.slf4j.Slf4j;
import org.PasswordManager.exceptions.HashErrorException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(HashErrorException.class)
    public final ResponseEntity<Object> handleHashErrorException(Exception exception, WebRequest request) {
        log.warn(exception.getMessage(), exception);
        return handleExceptionInternal(
            exception,
            exception.getMessage(),
            new HttpHeaders(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            request
        );
    }
}
