package org.PasswordManager.exceptions.app;

import org.springframework.stereotype.Component;

@Component
public class DifferentAppConfigurationException extends RuntimeException{
    public DifferentAppConfigurationException() {
        super("Configurations are not matching!");
        this.setStackTrace(new StackTraceElement[0]);
    }
}
