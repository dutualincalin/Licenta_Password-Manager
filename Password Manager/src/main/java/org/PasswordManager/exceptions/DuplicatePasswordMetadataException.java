package org.PasswordManager.exceptions;

import org.springframework.stereotype.Component;

@Component
public class DuplicatePasswordMetadataException extends RuntimeException{
    public DuplicatePasswordMetadataException() {
        super("[WARNING]: This password metadata entry already exists.");
    }
}
