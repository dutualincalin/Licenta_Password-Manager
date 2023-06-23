package org.PasswordManager.service;

import com.github.fzakaria.ascii85.Ascii85;
import org.PasswordManager.model.PasswordConfiguration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {
    private final TextEncryptor textEncryptor;
    private final Pbkdf2PasswordEncoder pbkdf2PasswordEncoder;
    private final Argon2PasswordEncoder argon2PasswordEncoder;

    public EncryptionService(){
        int PBKDF2_HASH_ITERATIONS = 500;
        int PBKDF2_HASH_SIZE = 256;

        int ARGON2_HASH_ITERATIONS = 10;
        int ARGON2_HASH_SIZE = 128;
        int ARGON2_THREAD_NUM = 8;
        int ARGON2_MEMORY = 65536;

        String token = "reganaMdrowssaP";
        String TEXT_SALT = "53616c747953616c7479726567616e614d64726f777373615053616c7453616c74";

        this.pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder(
                token + "pbkdf2",
            PBKDF2_HASH_ITERATIONS,
            PBKDF2_HASH_SIZE
        );

        pbkdf2PasswordEncoder.setEncodeHashAsBase64(true);

        this.argon2PasswordEncoder = new Argon2PasswordEncoder(
                0,
            ARGON2_HASH_SIZE,
            ARGON2_THREAD_NUM,
            ARGON2_MEMORY,
            ARGON2_HASH_ITERATIONS
        );

        textEncryptor = Encryptors.delux(token + "config", TEXT_SALT);
    }


    /**
     ** Encryption methods
     ************************************************************************************/

    public String encryptImage(String imageString) {
        return pbkdf2PasswordEncoder.encode(imageString);
    }

    public String encryptPassword(String imgHash, PasswordConfiguration passwordConfiguration, String master) {
        return argon2PasswordEncoder
            .encode(imgHash + passwordConfiguration.toString() + master);
    }

    public String encodePassword(String password) {
        return Ascii85.encode(password.getBytes());
    }

    public String encryptText(String text) {
        return textEncryptor.encrypt(text);
    }


    /**
     ** Decryption method
     ************************************************************************************/
    public String decryptText(String hash) {
        return textEncryptor.decrypt(hash);
    }
}
