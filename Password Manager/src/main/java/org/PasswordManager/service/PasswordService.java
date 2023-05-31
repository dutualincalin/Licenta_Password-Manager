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

    public String generatePassword(String master, PasswordMetadata passwordMetadata) {
        return encryptionService.encryptPassword(
            configurationService.getConfigurationImage(),
            passwordMetadata,
            master
        ).substring(0, passwordMetadata.getLength());
    }

    public void setPasswordMetadataList(ArrayList<PasswordMetadata> passwordsMetadata) {
        this.passwordMetadataList = passwordsMetadata;
    }

    public ArrayList<PasswordMetadata> getPasswordMetadataList() {
        return passwordMetadataList;
    }

    public void addPasswordMetadata(PasswordMetadata passwordMetadata) {
        passwordMetadataList.add(passwordMetadata);
    }

    public void removePasswordMetadata(PasswordMetadata passwordMetadata) {
        passwordMetadataList.remove(passwordMetadata);
    }
}
