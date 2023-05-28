package org.PasswordManager.service;

import org.PasswordManager.exceptions.ConfigurationIncompleteException;
import org.PasswordManager.utility.Utils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

@Service
public class ConfigurationService {
    private String hashImg;

    private final EncryptionService encryptionService;

    private final ConfigurationIncompleteException configurationIncompleteException;

    public ConfigurationService(EncryptionService encryptionService,
                                ConfigurationIncompleteException configurationIncompleteException) {
        this.encryptionService = encryptionService;
        this.configurationIncompleteException = configurationIncompleteException;
        hashImg = null;

        if(new File(Utils.configFileName).exists()) {
            // read configuration file
        } else {
            hashImg = null;
        }
    }

    public void setConfigurationImage(String path) {
        BufferedImage configImg = null;

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

    public void saveConfigurationToFile(String stringToWrite){
        FileWriter writer;

        try {
            writer = new FileWriter(Utils.configFileName);
            writer.write(stringToWrite);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String gatherConfigurationFromFile(){
        String gatheredString;

        try {
            Scanner scanner = new Scanner(new File(Utils.configFileName));
            gatheredString = scanner.nextLine();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return gatheredString;
    }

    public void shareConfiguration() {

    }

    public void saveConfiguration() {

    }

    public String gatherConfiguration()
    {
        return "";
    }
}
