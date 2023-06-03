package org.PasswordManager.exceptions;

import org.springframework.stereotype.Component;

@Component
public class WrongPathException extends RuntimeException{
    public WrongPathException(){
        super("[ERROR]: The path is wrong or the file doesn't exist.");
    }
}