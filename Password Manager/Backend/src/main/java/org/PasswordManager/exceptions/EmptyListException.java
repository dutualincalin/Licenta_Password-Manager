package org.PasswordManager.exceptions;

import org.springframework.stereotype.Component;

@Component
public class EmptyListException extends RuntimeException{
    public EmptyListException() {
        super("The list with configurations is empty");
    }
}
