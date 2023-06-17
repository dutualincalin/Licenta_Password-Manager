package org.PasswordManager.exceptions;

import org.springframework.stereotype.Component;

@Component
public class InternalServerException extends RuntimeException{
    public InternalServerException() {
        super("[ERROR]: Internal Sever error!");
    }
}
