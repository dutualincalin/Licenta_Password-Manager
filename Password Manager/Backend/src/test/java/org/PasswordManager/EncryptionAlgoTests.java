package org.PasswordManager;

import org.PasswordManager.model.PasswordConfiguration;
import org.PasswordManager.service.EncryptionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.Date;

@SpringBootTest
public class EncryptionAlgoTests {
    @Autowired
    EncryptionService encryptionService;

    @Test
    public void checkPBKDF2Algo() {
        int PBKDF2_HASH_ITERATIONS = 500;
        int PBKDF2_HASH_SIZE = 256;

        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder(
            "reganaMdrowssaP",
            PBKDF2_HASH_ITERATIONS,
            PBKDF2_HASH_SIZE
        );

        pbkdf2PasswordEncoder.setEncodeHashAsBase64(true);
        Assertions.assertNotEquals(
            pbkdf2PasswordEncoder.encode("password"),
            pbkdf2PasswordEncoder.encode("password")
        );
    }

    @Test
    public void checkARGON2Algo() {
        int ARGON2_HASH_ITERATIONS = 10;
        int ARGON2_HASH_SIZE = 64;
        int ARGON2_THREAD_NUM = 4;
        int ARGON2_MEMORY = 1048576;

        PasswordConfiguration passParams = new PasswordConfiguration(
            "www.google.com",
            "CheckyCheckyCheckCheck",
            3,
            16,
            new Date()
        );

        Argon2PasswordEncoder argon2PasswordEncoder = new Argon2PasswordEncoder(
            0,
            ARGON2_HASH_SIZE,
            ARGON2_THREAD_NUM,
            ARGON2_MEMORY,
            ARGON2_HASH_ITERATIONS
        );

        String startHash = passParams.toString();

        Assertions.assertEquals(
            argon2PasswordEncoder.encode(
                "81Dz7E1M/X5/wPVaLVgA/xg71p8tWHOvuTnVlhAjamPOJIGUAX8x1Q==" +
                    "NotIdealForYOU" + startHash),
            argon2PasswordEncoder.encode(
                "81Dz7E1M/X5/wPVaLVgA/xg71p8tWHOvuTnVlhAjamPOJIGUAX8x1Q==" +
                    "NotIdealForYOU" + startHash)
        );
    }

    @Test
    public void checkAESAlgo() {
        String testString = "TestingString";

        Assertions.assertNotEquals(
            encryptionService.encryptText(testString),
            encryptionService.encryptText(testString)
        );

        Assertions.assertEquals(
            testString,
            encryptionService.decryptText(encryptionService.encryptText(testString))
        );
    }
}
