package org.PasswordManager.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordMetadata {
    private final String website;
    private String username;
    private int version;
    private int length;

    public PasswordMetadata(String website){
        this.website = website;
        username = null;
        version = 0;
        length = 0;
    }

    @Override
    public String toString() {
        String stringParams = website;

        if(username != null){
            stringParams += username;
        }

        if(version != 0){
            stringParams += version;
        }

        if(length != 0){
            stringParams += length;
        }

        return stringParams;
    }
}
