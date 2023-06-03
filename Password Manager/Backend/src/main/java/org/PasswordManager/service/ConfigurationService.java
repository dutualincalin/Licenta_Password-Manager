package org.PasswordManager.service;

import org.PasswordManager.exceptions.ConfigurationIncompleteException;
import org.PasswordManager.exceptions.WrongPathException;
import org.PasswordManager.mapper.PasswordMapper;
import org.PasswordManager.model.PasswordMetadata;
import org.PasswordManager.utility.Utils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Service
public class ConfigurationService {
    private String hashImg;

    private final EncryptionService encryptionService;

    private final IOService ioService;

    private final ConfigurationIncompleteException configurationIncompleteException;

    private final WrongPathException wrongPathException;

    public ConfigurationService(EncryptionService encryptionService,
                                IOService ioService,
                                ConfigurationIncompleteException configurationIncompleteException,
                                WrongPathException wrongPathException) {
        this.encryptionService = encryptionService;
        this.ioService = ioService;
        this.configurationIncompleteException = configurationIncompleteException;
        this.wrongPathException = wrongPathException;
        hashImg = null;
    }

    public void setConfigurationImage(String path) {
        BufferedImage configImg;

        try {
            configImg = ImageIO.read(new File(path));
        } catch (IOException e) {
            throw wrongPathException;
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

    public String exportConfigToQR(
        ArrayList<PasswordMetadata> passwordMetadataList
    ) {
        String configuration = getConfigurationImage()
            + PasswordMapper.instance.passwordMetadataListToJSON(passwordMetadataList);
        String QRName  = "QR_"+ Utils.QR_DATE_FORMAT.format(new Date()) + ".png";


        ioService.createQR(encryptionService.encryptText(configuration), QRName);

        return "./QRCodes/" + QRName;
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
