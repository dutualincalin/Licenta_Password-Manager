package org.PasswordManager;

import org.PasswordManager.service.IOService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ConfigurationTests {
    @Autowired
    IOService ioService;

    @Test
    public void testQRWriting() {
        ioService.createQR("Hello, it's me from the past!", "QRTest");
    }

    @Test
    public void testQRReading() {
        System.out.println(ioService.readQR("./QRCodes/QRTest.png"));
    }

    @Test
    public void testConfigurationSaving() {

    }

    @Test
    public void testConfigurationGathering() {

    }
}
