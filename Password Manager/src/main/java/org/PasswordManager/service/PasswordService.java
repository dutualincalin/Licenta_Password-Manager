package org.PasswordManager.service;

import org.PasswordManager.model.PasswordMetadata;
import org.PasswordManager.utility.Utils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;

@Service
public class PasswordService {

    private final EncryptionService encryptionService;

    private final ConfigurationService configurationService;

    private ArrayList<PasswordMetadata> passwordMetadataList;

    public PasswordService(EncryptionService encryptionService,
                           ConfigurationService configurationService) {
        this.encryptionService = encryptionService;
        this.configurationService = configurationService;

        if(new File(Utils.CONFIG_FILE_NAME).exists()) {
            passwordMetadataList = configurationService.gatherConfiguration();
        } else {
            passwordMetadataList = new ArrayList<>();
        }
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

        passwordMetadataList.add(passwordMetadata);

        return encryptionService.encryptPassword(
            configurationService.getConfigurationImage(),
            passwordMetadata,
            master
        ).substring(0, length);
    }

    public void setPasswordsMetadata(ArrayList<PasswordMetadata> passwordsMetadata) {
        this.passwordMetadataList = passwordsMetadata;
    }

    public ArrayList<PasswordMetadata> getPasswordsMetadata() {
        return passwordMetadataList;
    }

    public void removePasswordMetadata(PasswordMetadata passwordMetadata) {
        passwordMetadataList.remove(passwordMetadata);
    }
}
