package org.PasswordManager.exceptions;

import org.springframework.stereotype.Component;

@Component
public class NoConfigException extends RuntimeException{
    public NoConfigException() {
        super("Configuration File Missing");
    }
}
