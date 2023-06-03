package org.PasswordManager.exceptions;

import org.springframework.stereotype.Component;

@Component
public class PasswordGenerationException extends RuntimeException{
    public PasswordGenerationException() {
        super("[WARNING]: The provided password metadata entry is not registered");
    }
}
