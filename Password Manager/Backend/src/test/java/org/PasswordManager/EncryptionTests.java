package org.PasswordManager;

import org.PasswordManager.mapper.PasswordMapper;
import org.PasswordManager.model.PasswordConfiguration;
import org.PasswordManager.service.ConfigurationService;
import org.PasswordManager.service.EncryptionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@SpringBootTest
public class EncryptionTests {
    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private ConfigurationService configurationService;

    PasswordMapper mapper = PasswordMapper.instance;

    @Test
    public void checkImageHashing() {
        PasswordMapper passwordMapper = PasswordMapper.instance;
        BufferedImage img = null;
        String imageName = "NFS.jpg";

        try {
            img = ImageIO.read(new File(imageName));
        } catch (IOException ignored) {
        }

        String hashedImage = encryptionService.encryptImage(
            passwordMapper.imageToBase64String(img, "jpg")
        );

        Assertions.assertNotEquals(
            hashedImage,
            encryptionService.encryptImage(passwordMapper.imageToBase64String(img, "jpg"))
        );
    }

    @Test
    public void checkPasswordHashing() {
        try {
            BufferedImage img = ImageIO.read(new File("NFS.jpg"));
            String imgData = mapper.imageToBase64String(img, "jpg");
            PasswordConfiguration passwordConfiguration = new PasswordConfiguration(
                "www.twitter.com",
                "Elon Musk",
                0,
                64,
                new Date()
            );

            Assertions.assertEquals(
                encryptionService.encryptPassword(imgData, passwordConfiguration, "test"),
                encryptionService.encryptPassword(imgData, passwordConfiguration, "test")
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void checkJSONAESHashing(){
        String testJSON = "{\n" +
            "\tfield1: test_value1,\n" +
            "\tfield2: [field2_1:test_value2_1, field2_2:test_value2_2]\n" +
            "}";

        String hashString = encryptionService.encryptText(testJSON);
        Assertions.assertEquals(testJSON, encryptionService.decryptText(hashString));
    }

    @Test
    public void checkImageAESHashing(){
        configurationService.setConfigurationImage("./NFS.jpg");

        String hash = encryptionService.encryptText(configurationService.getConfigurationImage());
        String imgDecrypted = encryptionService.decryptText(hash);

        Assertions.assertEquals(configurationService.getConfigurationImage(), imgDecrypted);
    }
}
