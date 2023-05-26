package org.PasswordManager.exceptions;

import org.springframework.stereotype.Component;

@Component
public class HashErrorException extends RuntimeException {
    public HashErrorException(){
        super("[ERROR]: The generated hash is corrupted.");
    }
}
