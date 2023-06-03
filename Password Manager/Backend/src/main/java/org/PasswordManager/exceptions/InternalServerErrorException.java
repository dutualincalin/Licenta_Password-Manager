package org.PasswordManager.exceptions;

import org.springframework.stereotype.Component;

@Component
public class InternalServerErrorException extends RuntimeException{
    public InternalServerErrorException() {
        super("[ERROR] Internal Sever error!");
    }
}
