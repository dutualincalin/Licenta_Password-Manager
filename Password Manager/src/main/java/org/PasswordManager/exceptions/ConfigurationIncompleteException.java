package org.PasswordManager.exceptions;

import org.springframework.stereotype.Component;

@Component
public class ConfigurationIncompleteException extends RuntimeException{
    public ConfigurationIncompleteException(){
        super("[ERROR]: App configuration is incomplete!");
    }
}
