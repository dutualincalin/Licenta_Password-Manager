package org.PasswordManager.exceptions;

import org.springframework.stereotype.Component;

@Component
public class WrongFilePathException extends RuntimeException{
    public WrongFilePathException(){
        super("[ERROR]: The path is wrong or the file doesn't exist.");
        this.setStackTrace(new StackTraceElement[0]);
    }
}