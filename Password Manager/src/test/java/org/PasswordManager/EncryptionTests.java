package org.PasswordManager;

import org.PasswordManager.model.PasswordParams;
import org.PasswordManager.service.EncryptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@SpringBootTest
public class EncryptionTests {
    @Autowired
    private EncryptionService encryptionService;

    @Test
    public void checkImageHashing() {
        BufferedImage img = null;
        String imageName = "Florea_The_God.jpg";

        try {
            img = ImageIO.read(new File(imageName));
        } catch (IOException ignored) {
        }

        String hashedImage = encryptionService.encryptImage(img, imageName.substring(imageName.length() - 3));
        System.out.println(hashedImage);
    }

    @Test
    public void checkPasswordHashing() {
        PasswordParams passParams = new PasswordParams(
            "81Dz7E1M/X5/wPVaLVgA/xg71p8tWHOvuTnVlhAjamPOJIGUAX8x1Q==",
            "NotIdealForYOU",
            "www.google.com"
        );

        passParams.setUsername("CheckyCheckyCheckCheck");
        passParams.setVersion(3);

        System.out.println(encryptionService.encryptObject(passParams));
    }
}
