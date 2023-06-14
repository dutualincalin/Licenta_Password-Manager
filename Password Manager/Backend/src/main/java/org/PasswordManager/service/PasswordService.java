package org.PasswordManager.service;

import org.PasswordManager.exceptions.DuplicatePasswordMetadataException;
import org.PasswordManager.exceptions.EmptyListException;
import org.PasswordManager.exceptions.InternalServerErrorException;
import org.PasswordManager.exceptions.MissingPasswordMetadataException;
import org.PasswordManager.exceptions.PasswordGenerationException;
import org.PasswordManager.exceptions.WrongPasswordMetadataExceptions;
import org.PasswordManager.model.PasswordMetadata;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PasswordService {

    private final EncryptionService encryptionService;

    private final ConfigurationService configurationService;

    private ArrayList<PasswordMetadata> passwordMetadataList;

    private final DuplicatePasswordMetadataException duplicatePasswordMetadataException;

    private final WrongPasswordMetadataExceptions wrongPasswordMetadataExceptions;

    private final InternalServerErrorException internalServerErrorException;

    private final PasswordGenerationException passwordGenerationException;

    private final EmptyListException emptyListException;

    private final MissingPasswordMetadataException missingPasswordMetadataException;

    public PasswordService(EncryptionService encryptionService,
                           ConfigurationService configurationService,
                           DuplicatePasswordMetadataException duplicatePasswordMetadataException,
                           WrongPasswordMetadataExceptions wrongPasswordMetadataExceptions,
                           InternalServerErrorException internalServerErrorException,
                           PasswordGenerationException passwordGenerationException,
                           EmptyListException emptyListException,
                           MissingPasswordMetadataException missingPasswordMetadataException) {
        this.encryptionService = encryptionService;
        this.configurationService = configurationService;
        this.duplicatePasswordMetadataException = duplicatePasswordMetadataException;
        this.wrongPasswordMetadataExceptions = wrongPasswordMetadataExceptions;
        this.internalServerErrorException = internalServerErrorException;
        this.passwordGenerationException = passwordGenerationException;
        this.emptyListException = emptyListException;
        this.missingPasswordMetadataException = missingPasswordMetadataException;
        passwordMetadataList = new ArrayList<>();
    }

    public void checkMetadata(PasswordMetadata passwordMetadata) {
        if(passwordMetadata.getVersion() < 0
            || passwordMetadata.getLength() < 16
            || passwordMetadata.getWebsite() == null
        ) {
            throw wrongPasswordMetadataExceptions;
        }
    }

    public String generatePassword(String master, String id) {
        PasswordMetadata passwordMetadata = this.passwordMetadataList.stream()
            .filter(passwordMeta -> passwordMeta.getId().equals(id))
            .findFirst().orElseThrow(MissingPasswordMetadataException::new);

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
            if(Objects.equals(passwordMetadata.getUsername(), "")){
                do {
                    passwordMetadata.setVersion(passwordMetadata.getVersion() + 1);
                } while(passwordMetadataList.contains(passwordMetadata));
            } else {
                throw duplicatePasswordMetadataException;
            }
        }

        passwordMetadataList.add(passwordMetadata);
    }

    public void removePasswordMetadata(String id) {
        if(passwordMetadataList.isEmpty()) {
            throw emptyListException;
        }

        if(passwordMetadataList.stream().noneMatch(
            passwordMetadata -> passwordMetadata.getId().equals(id)
        )) {
            throw missingPasswordMetadataException;
        }

        passwordMetadataList = (ArrayList<PasswordMetadata>) passwordMetadataList
            .stream().filter(passwordMetadata -> !passwordMetadata.getId().equals(id))
            .collect(
                Collectors.toList());
    }
}
