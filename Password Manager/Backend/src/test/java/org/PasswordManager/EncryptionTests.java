package org.PasswordManager;

import org.PasswordManager.mapper.PasswordMapper;
import org.PasswordManager.model.PasswordConfiguration;
import org.PasswordManager.service.ConfigurationService;
import org.PasswordManager.service.EncryptionService;
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

    @Test
    public void checkImageHashing() {
        PasswordMapper passwordMapper = PasswordMapper.instance;
        BufferedImage img = null;
        String imageName = "Florea_The_God.jpg";

        try {
            img = ImageIO.read(new File(imageName));
        } catch (IOException ignored) {
        }

        String hashedImage = encryptionService.encryptImage(
            passwordMapper.imageToBase64String(img, "jpg")
        );
        System.out.println(hashedImage);
    }

    @Test
    public void checkPasswordHashing() {
        PasswordConfiguration passParams = new PasswordConfiguration(
            "www.google.com",
            null,
            0,
            16,
            new Date()
        );

        passParams.setUsername("CheckyCheckyCheckCheck");
        passParams.setVersion(3);

        System.out.println(encryptionService.encryptPassword(
            "81Dz7E1M/X5/wPVaLVgA/xg71p8tWHOvuTnVlhAjamPOJIGUAX8x1Q==",
            passParams,
            "NotIdealForYOU")
        );
    }

    @Test
    public void checkJSONAESHashing(){
        String testString = "{\n" +
            "\tfield1: test_value1,\n" +
            "\tfield2: [field2_1:test_value2_1, field2_2:test_value2_2]\n" +
            "}";

        String hashString = encryptionService.encryptText(testString);
        System.out.println(hashString);

        testString = encryptionService.decryptText(hashString);
        System.out.println(testString);
    }

    @Test
    public void checkImageAESHashing(){
        configurationService.setConfigurationImage("./NFS.jpg");
        System.out.println(configurationService.getConfigurationImage());

        String hash = encryptionService.encryptText(configurationService.getConfigurationImage());
        System.out.println(hash);

        String imgDecrypted = encryptionService.decryptText(hash);
        System.out.println(imgDecrypted);
    }
}
