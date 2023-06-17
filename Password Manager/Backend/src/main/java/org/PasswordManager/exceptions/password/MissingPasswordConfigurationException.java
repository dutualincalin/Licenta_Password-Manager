package org.PasswordManager.exceptions.password;

import org.springframework.stereotype.Component;

@Component
public class MissingPasswordConfigurationException extends RuntimeException{
    public MissingPasswordConfigurationException() {
        super("Password configuration doesn't exist");
        this.setStackTrace(new StackTraceElement[0]);
    }
}
