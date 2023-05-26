package org.PasswordManager.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordParams {
    private final String imgHash;
    private final String website;
    private final String master;
    private String username;
    private int version;
    private int length;

    public PasswordParams(String imgHash, String master, String website){
        this.imgHash = imgHash;
        this.website = website;
        this.master = master;
        username = null;
        version = 0;
        length = 0;
    }

    @Override
    public String toString() {
        String stringParams = "";

        if(imgHash != null){
            stringParams += imgHash;
        }

        stringParams +=  website + master;

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
