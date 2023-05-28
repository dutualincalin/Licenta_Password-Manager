package org.PasswordManager.service;

import org.PasswordManager.model.PasswordMetadata;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PasswordService {

    private final EncryptionService encryptionService;

    private final ConfigurationService configurationService;

    private ArrayList<PasswordMetadata> passwordsMetadata;

    public PasswordService(EncryptionService encryptionService,
                           ConfigurationService configurationService) {
        this.encryptionService = encryptionService;
        this.configurationService = configurationService;
        passwordsMetadata = new ArrayList<>();
    }

    public String generatePassword(String master, String website, String username, int version,
                                   int length) {

        PasswordMetadata passwordMetadata = new PasswordMetadata(website);

        if(username != null){
            passwordMetadata.setUsername(username);
        }

        if(version != 0){
            passwordMetadata.setVersion(0);
        }

        if(length != 0){
            passwordMetadata.setLength(0);
        }

        return encryptionService.encryptPassword(
            configurationService.getConfigurationImage(),
            passwordMetadata,
            master
        ).substring(0, length);
    }

    public void setPasswordsMetadata(ArrayList<PasswordMetadata> passwordsMetadata) {
        this.passwordsMetadata = passwordsMetadata;
    }

    public ArrayList<PasswordMetadata> getPasswordsMetadata() {
        return passwordsMetadata;
    }
}
