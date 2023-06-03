package org.PasswordManager.service;

import org.PasswordManager.exceptions.DuplicatePasswordMetadataException;
import org.PasswordManager.exceptions.InternalServerErrorException;
import org.PasswordManager.exceptions.MissingPasswordMetadataException;
import org.PasswordManager.exceptions.PasswordGenerationException;
import org.PasswordManager.exceptions.WrongPasswordMetadataExceptions;
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

    private final DuplicatePasswordMetadataException duplicatePasswordMetadataException;

    private final MissingPasswordMetadataException missingPasswordMetadataException;

    private final WrongPasswordMetadataExceptions wrongPasswordMetadataExceptions;

    private final InternalServerErrorException internalServerErrorException;

    private final PasswordGenerationException passwordGenerationException;

    public PasswordService(EncryptionService encryptionService,
                           ConfigurationService configurationService,
                           DuplicatePasswordMetadataException duplicatePasswordMetadataException,
                           MissingPasswordMetadataException missingPasswordMetadataException,
                           WrongPasswordMetadataExceptions wrongPasswordMetadataExceptions,
                           InternalServerErrorException internalServerErrorException,
                           PasswordGenerationException passwordGenerationException) {
        this.encryptionService = encryptionService;
        this.configurationService = configurationService;
        this.duplicatePasswordMetadataException = duplicatePasswordMetadataException;
        this.missingPasswordMetadataException = missingPasswordMetadataException;
        this.wrongPasswordMetadataExceptions = wrongPasswordMetadataExceptions;
        this.internalServerErrorException = internalServerErrorException;
        this.passwordGenerationException = passwordGenerationException;

        if(new File(Utils.CONFIG_FILE_NAME).exists()) {
            passwordMetadataList = configurationService.gatherConfiguration();
        } else {
            passwordMetadataList = new ArrayList<>();
        }
    }

    public void checkMetadata(PasswordMetadata passwordMetadata) {
        if(passwordMetadata.getVersion() < 0
            || passwordMetadata.getLength() < 16
            || passwordMetadata.getWebsite() == null
        ) {
            throw wrongPasswordMetadataExceptions;
        }
    }

    public String generatePassword(String master, PasswordMetadata passwordMetadata) {
        checkMetadata(passwordMetadata);

        if(passwordMetadataList.stream().noneMatch
            (passwordMetadataItem -> passwordMetadataItem.equals(passwordMetadata)
                && passwordMetadataItem.getCreationDate().equals(passwordMetadata.getCreationDate())
            )
        ){
            throw wrongPasswordMetadataExceptions;
        }

        if(!passwordMetadataList.contains(passwordMetadata)){
            throw passwordGenerationException;
        }

        String hash = encryptionService.encryptPassword(
            configurationService.getConfigurationImage(),
            passwordMetadata,
            master
        );

        int index = hash.indexOf("$$") + 2;
        if (index == 1) {
            throw internalServerErrorException;
        }

        return hash.substring(index, index + passwordMetadata.getLength());
    }

    public void setPasswordMetadataList(ArrayList<PasswordMetadata> passwordsMetadataList) {
        this.passwordMetadataList = passwordsMetadataList;
    }

    public ArrayList<PasswordMetadata> getPasswordMetadataList() {
        return passwordMetadataList;
    }

    public void addPasswordsToMetadataList(ArrayList<PasswordMetadata> passwordMetadataList) {
        passwordMetadataList.forEach(
            passwordMetadata -> {
                if (!this.passwordMetadataList.contains(passwordMetadata)) {
                    this.passwordMetadataList.add(passwordMetadata);
                }
            }
        );
    }

    public void addPasswordMetadata(PasswordMetadata passwordMetadata) {
        checkMetadata(passwordMetadata);

        if(passwordMetadataList.contains(passwordMetadata)) {
            if(passwordMetadata.getUsername() == null){
                do {
                    passwordMetadata.setVersion(passwordMetadata.getVersion() + 1);
                } while(passwordMetadataList.contains(passwordMetadata));
            }

            else throw duplicatePasswordMetadataException;
        }
        passwordMetadataList.add(passwordMetadata);
    }

    public void removePasswordMetadata(PasswordMetadata passwordMetadata) {
        checkMetadata(passwordMetadata);

        if(!passwordMetadataList.contains(passwordMetadata)) {
            throw missingPasswordMetadataException;
        }

        passwordMetadataList.remove(passwordMetadata);
    }
}
