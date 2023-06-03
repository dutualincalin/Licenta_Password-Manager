package org.PasswordManager.exceptions;

import org.springframework.stereotype.Component;

@Component
public class MissingPasswordMetadataException extends RuntimeException{
    public MissingPasswordMetadataException() {
        super("[WARNING] This password metadata entry doesn't exist.");
    }
}
