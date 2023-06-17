package org.PasswordManager.exceptions.password;

import org.springframework.stereotype.Component;

@Component
public class DuplicatePasswordConfigurationException extends RuntimeException{
    public DuplicatePasswordConfigurationException() {
        super("This password metadata entry already exists.");
        this.setStackTrace(new StackTraceElement[0]);
    }
}
