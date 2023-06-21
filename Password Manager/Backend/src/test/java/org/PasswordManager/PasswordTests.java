package org.PasswordManager;

import org.PasswordManager.exceptions.password.DuplicatePasswordConfigurationException;
import org.PasswordManager.model.PasswordConfiguration;
import org.PasswordManager.service.PasswordService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;

@SpringBootTest
public class PasswordTests {
    @Autowired
    PasswordService passwordService;

    @Autowired
    DuplicatePasswordConfigurationException duplicatePasswordConfigurationException;

    @BeforeEach
    public void setUp() {
        ArrayList<PasswordConfiguration> passwordList = new ArrayList<>();
        passwordList.add(new PasswordConfiguration( "www.facebook.com", "facebookUser", 0,16, new Date()));
        passwordList.add(new PasswordConfiguration("www.pinterest.com", "pinterestGuy", 0,64, new Date()));
        passwordList.add(new PasswordConfiguration("www.twitter.com", "Elon Musk", 0,64, new Date()));

        passwordService.setPasswordConfigurationList(passwordList);
    }

    @Test
    public void testAddPasswordConfig() {
        passwordService.addPasswordConfiguration(
            new PasswordConfiguration("www.twitter.com", "Elon Musk", 1,64, new Date())
        );

        Assertions.assertEquals(passwordService.getPasswordConfigurationList().size(), 4);
    }

    @Test
    public void testListGathering() {
        Assertions.assertEquals(3, passwordService.getPasswordConfigurationList().size());
    }

    @Test
    public void testDeletePasswordConfig() {
        passwordService.removePasswordConfiguration(
            passwordService.getPasswordConfigurationList().get(0).getId()
        );

        Assertions.assertEquals(2, passwordService.getPasswordConfigurationList().size());
    }
}
