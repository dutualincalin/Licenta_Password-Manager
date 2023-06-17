package org.PasswordManager.controller;

import lombok.extern.slf4j.Slf4j;
import org.PasswordManager.exceptions.app.IncompleteAppConfigurationException;
import org.PasswordManager.exceptions.app.DifferentAppConfigurationException;
import org.PasswordManager.exceptions.password.DuplicatePasswordConfigurationException;
import org.PasswordManager.exceptions.password.EmptyPasswordConfigurationListException;
import org.PasswordManager.exceptions.ExceededQRCapacityException;
import org.PasswordManager.exceptions.InternalServerException;
import org.PasswordManager.exceptions.password.MissingPasswordConfigurationException;
import org.PasswordManager.exceptions.WrongMetadataException;
import org.PasswordManager.exceptions.WrongFilePathException;
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
    @ExceptionHandler(InternalServerException.class)
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

    @ExceptionHandler(IncompleteAppConfigurationException.class)
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

    @ExceptionHandler({
        WrongFilePathException.class,
        DuplicatePasswordConfigurationException.class,
        WrongMetadataException.class,
        EmptyPasswordConfigurationListException.class,
        MissingPasswordConfigurationException.class,
        DifferentAppConfigurationException.class
    })
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

    @ExceptionHandler({ExceededQRCapacityException.class})
    public final ResponseEntity<Object> handleExceededQRCapacityException(
        ExceededQRCapacityException exception,
        WebRequest request
    ) {
        log.warn(exception.getMessage() + exception.getExceedingCapacity());
        return handleExceptionInternal(
            exception,
            exception.getMessage() + exception.getExceedingCapacity(),
            new HttpHeaders(),
            HttpStatus.BAD_REQUEST,
            request
        );
    }
}