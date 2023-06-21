package org.PasswordManager;

import org.PasswordManager.mapper.PasswordMapper;
import org.PasswordManager.model.PasswordConfiguration;
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
    public void testConfigurationSaving() {
        configurationService.setConfigurationImage("./NFS.jpg");
        passwordService.addPasswordConfiguration(new PasswordConfiguration( "www.facebook.com", "facebookUser", 0,16, new Date()));
        passwordService.addPasswordConfiguration(new PasswordConfiguration("www.pinterest.com", "pinterestGuy", 0,64, new Date()));
        passwordService.addPasswordConfiguration(new PasswordConfiguration("www.twitter.com", "Elon Musk", 0,64, new Date()));

        configurationService.saveConfiguration(passwordService.getPasswordConfigurationList());
        System.out.println(configurationService.getConfigurationImage()
            + PasswordMapper.instance.passwordConfiguratonListToJSON(passwordService.getPasswordConfigurationList())
        );
    }

    @Test
    public void testConfigurationGathering() {
        System.out.println(configurationService.getConfigurationImage()
            + PasswordMapper.instance.passwordConfiguratonListToJSON(passwordService.getPasswordConfigurationList())
        );
    }
}
