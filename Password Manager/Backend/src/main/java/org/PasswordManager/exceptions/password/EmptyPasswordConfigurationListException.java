package org.PasswordManager.exceptions.password;

import org.springframework.stereotype.Component;

@Component
public class EmptyPasswordConfigurationListException extends RuntimeException{
    public EmptyPasswordConfigurationListException() {
        super("The list with configurations is empty");
        this.setStackTrace(new StackTraceElement[0]);
    }
}
