package org.PasswordManager.controller;

import lombok.extern.slf4j.Slf4j;
import org.PasswordManager.exceptions.ConfigurationIncompleteException;
import org.PasswordManager.exceptions.DuplicatePasswordMetadataException;
import org.PasswordManager.exceptions.EmptyListException;
import org.PasswordManager.exceptions.ExceededQRCapacityException;
import org.PasswordManager.exceptions.InternalServerErrorException;
import org.PasswordManager.exceptions.MissingPasswordMetadataException;
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

    @ExceptionHandler({
        WrongPathException.class,
        DuplicatePasswordMetadataException.class,
        WrongPasswordMetadataExceptions.class,
        PasswordGenerationException.class,
        EmptyListException.class,
        MissingPasswordMetadataException.class
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


// TODO: Write this in simple exception fields
//@Override
//public String toString() {
//    StackTraceElement[] stackTrace = getStackTrace();
//    if (stackTrace != null && stackTrace.length > 0) {
//        return getClass().getName() + ": " + stackTrace[0].toString();
//    }
//    return super.toString();
//}