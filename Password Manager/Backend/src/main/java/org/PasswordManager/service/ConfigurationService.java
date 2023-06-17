package org.PasswordManager.service;

import lombok.extern.slf4j.Slf4j;
import org.PasswordManager.exceptions.app.IncompleteAppConfigurationException;
import org.PasswordManager.exceptions.app.DifferentAppConfigurationException;
import org.PasswordManager.mapper.PasswordMapper;
import org.PasswordManager.model.PasswordConfiguration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class ConfigurationService {
    private String configImgHash;

    private final IOService ioService;
    private final EncryptionService encryptionService;

    private final DifferentAppConfigurationException differentAppConfigurationException;
    private final IncompleteAppConfigurationException incompleteAppConfigurationException;

    public ConfigurationService(EncryptionService encryptionService,
                                IOService ioService,
                                IncompleteAppConfigurationException incompleteAppConfigurationException,
                                DifferentAppConfigurationException differentAppConfigurationException) {
        this.encryptionService = encryptionService;
        this.ioService = ioService;
        this.incompleteAppConfigurationException = incompleteAppConfigurationException;
        this.differentAppConfigurationException = differentAppConfigurationException;
        configImgHash = null;
    }


    /**
     ** Configuration image methods
     ************************************************************************************/

    public void setConfigurationImage(String imgData) {
        if(imgData.equals("")) {
            throw incompleteAppConfigurationException;
        }

        String pattern = "data:image/\\w+;base64,";
        configImgHash = encryptionService.encryptImage(imgData.replaceFirst(pattern, ""));
    }

    public String getConfigurationImage() {
        if(configImgHash == null){
            throw incompleteAppConfigurationException;
        }

        return configImgHash;
    }


    /**
     ** QR data setting and getting methods
     ************************************************************************************/

    public byte[] exportConfigToQR(ArrayList<PasswordConfiguration> passwordConfigurationList) {
        String configuration = getConfigurationImage()
            + PasswordMapper.instance.passwordConfiguratonListToJSON(passwordConfigurationList);

        return ioService.createQR(encryptionService.encryptText(configuration));
    }

    public ArrayList<PasswordConfiguration> readConfigFromQR(String qrData) {
        ArrayList<PasswordConfiguration> passwordConfigurationList;
        String configuration = encryptionService.decryptText(qrData);

        try {
            String passMetaList = configuration.substring(configuration.indexOf("["));
            String QRHashImg = configuration.substring(0, configuration.indexOf("["));

            if(configImgHash == null) {
                this.configImgHash = QRHashImg;
            } else {
                if(!configImgHash.equals(QRHashImg)) {
                    throw differentAppConfigurationException;
                }
            }

            passwordConfigurationList = PasswordMapper.instance.jsonToPasswordConfigurationList(passMetaList);
        } catch(IndexOutOfBoundsException e) {
            configImgHash = configuration;
            passwordConfigurationList = new ArrayList<>();
        }

        return passwordConfigurationList;
    }


    /**
     ** App configuration methods
     ************************************************************************************/

    public void saveConfiguration(ArrayList<PasswordConfiguration> passwordConfigurationList) {
        String configuration = getConfigurationImage()
            + PasswordMapper.instance.passwordConfiguratonListToJSON(passwordConfigurationList);

        ioService.saveConfigToFile(encryptionService.encryptText(configuration));
    }

    public ArrayList<PasswordConfiguration> gatherConfiguration() {
        String fileString = ioService.gatherConfigFromFile();
        String configuration = encryptionService.decryptText(fileString);
        configImgHash = configuration.substring(0, configuration.indexOf("["));

        return PasswordMapper.instance.jsonToPasswordConfigurationList(
            configuration.substring(configuration.indexOf("["))
        );
    }
}
