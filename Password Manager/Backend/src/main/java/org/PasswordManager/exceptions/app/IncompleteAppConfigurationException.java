package org.PasswordManager.exceptions.app;

import org.springframework.stereotype.Component;

@Component
public class IncompleteAppConfigurationException extends RuntimeException{
    public IncompleteAppConfigurationException(){
        super("App configuration is incomplete! Please close and reopen the website");
        this.setStackTrace(new StackTraceElement[0]);
    }
}
