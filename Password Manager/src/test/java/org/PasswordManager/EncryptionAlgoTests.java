package org.PasswordManager;

import org.PasswordManager.model.PasswordMetadata;
import org.PasswordManager.utility.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.Date;

@SpringBootTest
public class EncryptionAlgoTests {
    @Test
    public void checkPBKDF2Algo() {
        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder(
            "reganaMdrowssaP",
            Utils.PBKDF2_HASH_ITERATIONS,
            Utils.PBKDF2_HASH_SIZE
        );

        pbkdf2PasswordEncoder.setEncodeHashAsBase64(true);
        System.out.println(pbkdf2PasswordEncoder.encode("password"));
    }

    @Test
    public void checkARGON2Algo() {
        PasswordMetadata passParams = new PasswordMetadata(
            "www.google.com",
            "CheckyCheckyCheckCheck",
            3,
            16,
            new Date()
        );

        Argon2PasswordEncoder argon2PasswordEncoder = new Argon2PasswordEncoder(
            0,
            Utils.ARGON2_HASH_SIZE,
            Utils.ARGON2_THREAD_NUM,
            Utils.ARGON2_MEMORY,
            Utils.ARGON2_HASH_ITERATIONS
        );

        String startHash = passParams.toString();
        System.out.println(argon2PasswordEncoder.encode(
            "81Dz7E1M/X5/wPVaLVgA/xg71p8tWHOvuTnVlhAjamPOJIGUAX8x1Q==" +
                "NotIdealForYOU" + startHash));
    }
}
