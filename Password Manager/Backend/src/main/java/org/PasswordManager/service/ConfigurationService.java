package org.PasswordManager.service;

import org.PasswordManager.exceptions.ConfigurationIncompleteException;
import org.PasswordManager.mapper.PasswordMapper;
import org.PasswordManager.model.PasswordMetadata;
import org.PasswordManager.utility.Utils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

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

    public void setConfigurationImage(String imgData) {
        String pattern = "data:image/\\w+;base64,";
        hashImg = encryptionService.encryptImage(imgData.replaceFirst(pattern, ""));
    }

    public String getConfigurationImage() {
        if(hashImg == null){
            throw configurationIncompleteException;
        }

        return hashImg;
    }

    public byte[] exportConfigToQR(
        ArrayList<PasswordMetadata> passwordMetadataList
    ) {
        String configuration = getConfigurationImage()
            + PasswordMapper.instance.passwordMetadataListToJSON(passwordMetadataList);
        String QRName  = "QR_" + Utils.QR_DATE_FORMAT.format(new Date()) + ".png";


        ioService.createQR(encryptionService.encryptText(configuration), QRName);

        return ioService.getImageData("./QRCodes/" + QRName);
    }

    public ArrayList<PasswordMetadata> readConfigFromQR(String QRPath) {
        ArrayList<PasswordMetadata> passwordMetadataList;
        String configuration = encryptionService.decryptText(ioService.readQR(QRPath));

        try {
            String passMetaList = configuration.substring(configuration.indexOf("["));

            hashImg = configuration.substring(0, configuration.indexOf("["));
            passwordMetadataList = PasswordMapper.instance.jsonToPasswordMetadataList(passMetaList);
        }
        catch(IndexOutOfBoundsException e) {
            hashImg = configuration;
            passwordMetadataList = new ArrayList<>();
        }

        return passwordMetadataList;
    }

    public void saveConfiguration(ArrayList<PasswordMetadata> passwordMetadataList) {
        String configuration = getConfigurationImage()
            + PasswordMapper.instance.passwordMetadataListToJSON(passwordMetadataList);

        ioService.saveConfigToFile(encryptionService.encryptText(configuration));
    }

    public ArrayList<PasswordMetadata> gatherConfiguration() {
        String fileString = ioService.gatherConfigFromFile();
        String configuration = encryptionService.decryptText(fileString);
        hashImg = configuration.substring(0, configuration.indexOf("["));

        return PasswordMapper.instance.jsonToPasswordMetadataList(
            configuration.substring(configuration.indexOf("["))
        );
    }
}
