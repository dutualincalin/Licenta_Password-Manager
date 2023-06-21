package org.PasswordManager.service;

import org.PasswordManager.exceptions.password.DuplicatePasswordConfigurationException;
import org.PasswordManager.exceptions.password.EmptyPasswordConfigurationListException;
import org.PasswordManager.exceptions.InternalServerException;
import org.PasswordManager.exceptions.password.MissingPasswordConfigurationException;
import org.PasswordManager.exceptions.password.WrongMasterPasswordException;
import org.PasswordManager.exceptions.password.WrongMetadataException;
import org.PasswordManager.model.PasswordConfiguration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PasswordService {
    private final EncryptionService encryptionService;
    private final ConfigurationService configurationService;

    private final Pattern websitePattern;
    private final Pattern usernamePattern;
    private final Pattern masterPasswordPattern;

    private ArrayList<PasswordConfiguration> passwordConfigurationList;

    private final WrongMetadataException wrongMetadataException;
    private final InternalServerException internalServerException;
    private final WrongMasterPasswordException wrongMasterPasswordException;
    private final MissingPasswordConfigurationException missingPasswordConfigurationException;
    private final EmptyPasswordConfigurationListException emptyPasswordConfigurationListException;
    private final DuplicatePasswordConfigurationException duplicatePasswordConfigurationException;

    public PasswordService(EncryptionService encryptionService,
                           ConfigurationService configurationService,
                           DuplicatePasswordConfigurationException duplicatePasswordConfigurationException,
                           WrongMetadataException wrongMetadataException,
                           InternalServerException internalServerException,
                           EmptyPasswordConfigurationListException emptyPasswordConfigurationListException,
                           MissingPasswordConfigurationException missingPasswordConfigurationException,
                           WrongMasterPasswordException wrongMasterPasswordException) {
        this.encryptionService = encryptionService;
        this.configurationService = configurationService;
        this.duplicatePasswordConfigurationException = duplicatePasswordConfigurationException;
        this.wrongMetadataException = wrongMetadataException;
        this.internalServerException = internalServerException;
        this.emptyPasswordConfigurationListException = emptyPasswordConfigurationListException;
        this.missingPasswordConfigurationException = missingPasswordConfigurationException;
        this.wrongMasterPasswordException = wrongMasterPasswordException;
        passwordConfigurationList = new ArrayList<>();

        websitePattern = Pattern.compile(
            "^(http://|https://)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]+.[a-z]{2,3}(/[a-z]+)*$"
        );

        usernamePattern = Pattern.compile("^[A-Za-z][A-Za-z0-9_ ]{0,20}$");
        masterPasswordPattern = Pattern.compile("[A-Za-z0-9_ ]+");
    }


    /**
     ** Password Configuration List methods
     ************************************************************************************/

    public void setPasswordConfigurationList(
        ArrayList<PasswordConfiguration> passwordsMetadataList)
    {
        this.passwordConfigurationList = passwordsMetadataList;
    }

    public ArrayList<PasswordConfiguration> getPasswordConfigurationList() {
        return passwordConfigurationList;
    }

    public void addPasswordsToConfigurationList(
        ArrayList<PasswordConfiguration> passwordConfigurationList)
    {
        passwordConfigurationList.forEach(
            passwordMetadata -> {
                if (!this.passwordConfigurationList.contains(passwordMetadata)) {
                    this.passwordConfigurationList.add(passwordMetadata);
                }
            }
        );
    }


    /**
    ** Password Configuration methods
    ************************************************************************************/

    public void addPasswordConfiguration(PasswordConfiguration passwordConfiguration) {
        checkMetadata(passwordConfiguration);

        if(passwordConfigurationList.contains(passwordConfiguration)) {
            if(Objects.equals(passwordConfiguration.getUsername(), "")){
                do {
                    passwordConfiguration.setVersion(passwordConfiguration.getVersion() + 1);
                } while(passwordConfigurationList.contains(passwordConfiguration));
            } else {
                throw duplicatePasswordConfigurationException;
            }
        }

        passwordConfigurationList.add(passwordConfiguration);
    }

    public void removePasswordConfiguration(String id) {
        if(passwordConfigurationList.isEmpty()) {
            throw emptyPasswordConfigurationListException;
        }

        if(passwordConfigurationList.stream().noneMatch(
            passwordMetadata -> passwordMetadata.getId().equals(id)
        )) {
            throw missingPasswordConfigurationException;
        }

        passwordConfigurationList = (ArrayList<PasswordConfiguration>) passwordConfigurationList
            .stream().filter(passwordMetadata -> !passwordMetadata.getId().equals(id))
            .collect(Collectors.toList());
    }

    public String generatePassword(String master, String id) {
        if(!masterPasswordPattern.matcher(master).matches()) {
            throw wrongMasterPasswordException;
        }

        PasswordConfiguration passwordConfiguration = this.passwordConfigurationList.stream()
            .filter(passwordMeta -> passwordMeta.getId().equals(id))
            .findFirst().orElseThrow(MissingPasswordConfigurationException::new);

        String hash = encryptionService.encryptPassword(
            configurationService.getConfigurationImage(),
            passwordConfiguration,
            master
        );

        int index = hash.indexOf("$$") + 2;
        if (index == 1) {
            throw internalServerException;
        }

        hash = encryptionService.encodePassword(
            hash.substring(index, index + passwordConfiguration.getLength())
        );

        while(hash.startsWith(".") || hash.startsWith("-")){
            hash = hash.substring(1);
        }

        return hash;
    }


    /**
     ** Other methods
     ************************************************************************************/

    public void checkMetadata(PasswordConfiguration passwordConfiguration) {
        if(passwordConfiguration.getVersion() < 0
            || (passwordConfiguration.getUsername() != null
                && !usernamePattern.matcher(passwordConfiguration.getUsername()).matches())
            || !websitePattern.matcher(passwordConfiguration.getWebsite()).matches()
        ) {
            throw wrongMetadataException;
        }
    }
}
