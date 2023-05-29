package org.PasswordManager.service;

import org.PasswordManager.exceptions.ConfigurationIncompleteException;
import org.PasswordManager.mapper.PasswordMapper;
import org.PasswordManager.model.PasswordMetadata;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Service
public class ConfigurationService {
    private String hashImg;

    private final EncryptionService encryptionService;

    private final IOService ioService;

    private final ConfigurationIncompleteException configurationIncompleteException;

    public ConfigurationService(EncryptionService encryptionService,
                                IOService ioService,
                                ConfigurationIncompleteException configurationIncompleteException) {
        this.encryptionService = encryptionService;
        this.ioService = ioService;
        this.configurationIncompleteException = configurationIncompleteException;
        hashImg = null;
    }

    public void setConfigurationImage(String path) {
        BufferedImage configImg;

        try {
            configImg = ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        hashImg = encryptionService.encryptImage(
            configImg,
            path.substring(path.length() - 3)
        );
    }

    public String getConfigurationImage() {
        if(hashImg == null){
            throw configurationIncompleteException;
        }

        return hashImg;
    }

    public void saveConfigurationToQR(
        ArrayList<PasswordMetadata> passwordMetadataList,
        String QRName
    ) {
        String configuration = hashImg
            + PasswordMapper.instance.passwordMetadataListToJSON(passwordMetadataList);

        ioService.createQR(
            encryptionService.encryptText(configuration), QRName);
    }

    public ArrayList<PasswordMetadata> readConfigurationFromQR(String QRPath) {
        String configuration = ioService.readQR(QRPath);
        hashImg = configuration.substring(0, configuration.indexOf("["));

        return PasswordMapper.instance.jsonToPasswordMetadataList(
            configuration.substring(configuration.indexOf("["))
        );
    }

    public void saveConfiguration(ArrayList<PasswordMetadata> passwordMetadataList) {
        String configuration = hashImg
            + PasswordMapper.instance.passwordMetadataListToJSON(passwordMetadataList);

        ioService.saveConfigToFile(encryptionService.encryptText(configuration));
    }

    public ArrayList<PasswordMetadata> gatherConfiguration() {
        String configuration = encryptionService.decryptText(ioService.gatherConfigFromFile());
        hashImg = configuration.substring(0, configuration.indexOf("["));

        return PasswordMapper.instance.jsonToPasswordMetadataList(
            configuration.substring(configuration.indexOf("["))
        );
    }
}
