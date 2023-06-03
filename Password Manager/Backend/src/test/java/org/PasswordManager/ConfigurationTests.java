package org.PasswordManager;

import org.PasswordManager.mapper.PasswordMapper;
import org.PasswordManager.model.PasswordMetadata;
import org.PasswordManager.service.ConfigurationService;
import org.PasswordManager.service.IOService;
import org.PasswordManager.service.PasswordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class ConfigurationTests {
    @Autowired
    IOService ioService;

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    PasswordService passwordService;

    @Test
    public void simpleTestQRWriting() {
        ioService.createQR("Hello, it's me from the past!", "QRTest");
    }

    @Test
    public void simpleTestQRReading() {
        System.out.println(ioService.readQR("./QRCodes/QRTest.png"));
    }

    @Test
    public void testConfigurationSaving() {
        configurationService.setConfigurationImage("./NFS.jpg");
        passwordService.addPasswordMetadata(new PasswordMetadata("www.facebook.com", "facebookUser", 0,16, new Date()));
        passwordService.addPasswordMetadata(new PasswordMetadata("www.pinterest.com", "pinterestGuy", 0,64, new Date()));
        passwordService.addPasswordMetadata(new PasswordMetadata("www.twitter.com", "Elon Musk", 0,64, new Date()));


        configurationService.saveConfiguration(passwordService.getPasswordMetadataList());
        System.out.println(configurationService.getConfigurationImage()
            + PasswordMapper.instance.passwordMetadataListToJSON(passwordService.getPasswordMetadataList())
        );
    }

    @Test
    public void testConfigurationGathering() {
        System.out.println(configurationService.getConfigurationImage()
            + PasswordMapper.instance.passwordMetadataListToJSON(passwordService.getPasswordMetadataList())
        );
    }

    @Test
    public void testQRWriting() {
        configurationService.setConfigurationImage("./NFS.jpg");
        passwordService.addPasswordMetadata(new PasswordMetadata("www.facebook.com", "facebookUser", 0,16, new Date()));
        passwordService.addPasswordMetadata(new PasswordMetadata("www.pinterest.com", "pinterestGuy", 0,64, new Date()));
        passwordService.addPasswordMetadata(new PasswordMetadata("www.twitter.com", "Elon Musk", 0,64, new Date()));

        System.out.println(configurationService.getConfigurationImage()
            + PasswordMapper.instance.passwordMetadataListToJSON(passwordService.getPasswordMetadataList())
        );
        System.out.println(configurationService.exportConfigToQR(passwordService.getPasswordMetadataList()));
    }

    @Test
    public void testQRReading() {
        passwordService.setPasswordMetadataList(configurationService.readConfigFromQR("./QRCodes/QR_03_06_2023_12_38_32_417.png"));
        System.out.println(configurationService.getConfigurationImage()
            + PasswordMapper.instance.passwordMetadataListToJSON(passwordService.getPasswordMetadataList())
        );
    }
}
