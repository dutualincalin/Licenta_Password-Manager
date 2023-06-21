package org.PasswordManager.exceptions.password;

import org.springframework.stereotype.Component;

@Component
public class WrongMasterPasswordException extends RuntimeException{
    public WrongMasterPasswordException(){
        super("Master Password must have alphanumeric, underline and space characters only");
    }
}
