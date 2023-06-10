package org.PasswordManager.controller;

import lombok.extern.slf4j.Slf4j;
import org.PasswordManager.exceptions.ConfigurationIncompleteException;
import org.PasswordManager.exceptions.DuplicatePasswordMetadataException;
import org.PasswordManager.exceptions.InternalServerErrorException;
import org.PasswordManager.exceptions.MissingPasswordMetadataException;
import org.PasswordManager.exceptions.NoConfigException;
import org.PasswordManager.exceptions.PasswordGenerationException;
import org.PasswordManager.exceptions.WrongPasswordMetadataExceptions;
import org.PasswordManager.exceptions.WrongPathException;
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
    @ExceptionHandler(NoConfigException.class)
    public final ResponseEntity<Object> handleNoConfigException(
        Exception exception,
        WebRequest request
    ) {
        log.warn(exception.getMessage(), exception);
        return handleExceptionInternal(
            exception,
            exception.getMessage(),
            new HttpHeaders(),
            HttpStatus.NOT_FOUND,
            request
        );
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public final ResponseEntity<Object> handleInternalServerExceptions(
        Exception exception,
        WebRequest request
    ) {
        log.error(exception.getMessage(), exception);
        return handleExceptionInternal(
            exception,
            exception.getMessage(),
            new HttpHeaders(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            request
        );
    }

    @ExceptionHandler(ConfigurationIncompleteException.class)
    public final ResponseEntity<Object> handleConfigurationIncompleteException(
        Exception exception,
        WebRequest request
    ) {
        log.warn(exception.getMessage(), exception);
        return handleExceptionInternal(
            exception,
            exception.getMessage(),
            new HttpHeaders(),
            HttpStatus.PRECONDITION_REQUIRED,
            request
        );
    }

    @ExceptionHandler({WrongPathException.class, DuplicatePasswordMetadataException.class,
        MissingPasswordMetadataException.class, WrongPasswordMetadataExceptions.class,
        PasswordGenerationException.class})
    public final ResponseEntity<Object> handleBadRequestExceptions(
        Exception exception,
        WebRequest request
    ) {
        log.warn(exception.getMessage(), exception);
        return handleExceptionInternal(
            exception,
            exception.getMessage(),
            new HttpHeaders(),
            HttpStatus.BAD_REQUEST,
            request
        );
    }
}
