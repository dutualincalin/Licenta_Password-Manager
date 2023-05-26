package org.PasswordManager.service;

import org.PasswordManager.model.PasswordParams;
import org.springframework.stereotype.Service;

@Service
public class GenerationService {
    // TODO: Do something about hashImg saving place
    private String hashImg;

    private final EncryptionService encryptionService;

    public GenerationService(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    public String generatePassword(
        String master,
        String website,
        String username,
        int version,
        int length
    ){
        PasswordParams passwordParams = new PasswordParams(hashImg, master, website);

        if(username != null){
            passwordParams.setUsername(username);
        }

        if(version != 0){
            passwordParams.setVersion(0);
        }

        if(length != 0){
            passwordParams.setLength(0);
        }

        return encryptionService.encryptObject(passwordParams).substring(0, length);
    }
}
