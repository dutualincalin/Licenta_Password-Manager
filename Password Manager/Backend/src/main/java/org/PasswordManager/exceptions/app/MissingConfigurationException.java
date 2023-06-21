package org.PasswordManager.exceptions.app;

import org.springframework.stereotype.Component;

@Component
public class MissingConfigurationException extends RuntimeException{
    public MissingConfigurationException(){
        super("[ERROR]: The configuration file doesn't exist");
        this.setStackTrace(new StackTraceElement[0]);
    }
}