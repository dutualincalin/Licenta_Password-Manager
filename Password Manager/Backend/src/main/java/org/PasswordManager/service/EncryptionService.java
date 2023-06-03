package org.PasswordManager.service;

import org.PasswordManager.mapper.PasswordMapper;
import org.PasswordManager.model.PasswordMetadata;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class EncryptionService {

    private final PasswordMapper mapper;

    private final Pbkdf2PasswordEncoder pbkdf2PasswordEncoder;

    private final TextEncryptor textEncryptor;

    private final Argon2PasswordEncoder argon2PasswordEncoder;

    public EncryptionService(){
        int PBKDF2_HASH_ITERATIONS = 500;
        int PBKDF2_HASH_SIZE = 256;

        int ARGON2_HASH_ITERATIONS = 10;
        int ARGON2_HASH_SIZE = 64;
        int ARGON2_THREAD_NUM = 4;
        int ARGON2_MEMORY = 1048576;

        this.mapper = PasswordMapper.instance;
        String token = "reganaMdrowssaP";


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

        textEncryptor = Encryptors.text(
            token + "config",
            "53616c747953616c7479726567616e614d64726f777373615053616c7453616c74"
        );
    }

    public String encryptImage(BufferedImage image, String type) {
        String imageString = mapper.imageToBase64String(image, type);
        return pbkdf2PasswordEncoder.encode(imageString);
    }

    public String encryptPassword(String imgHash, PasswordMetadata passwordMetadata, String master) {
        return argon2PasswordEncoder.encode(
            imgHash + passwordMetadata.toString() + master
        );
    }

    public String encryptText(String text) {
        return textEncryptor.encrypt(text);
    }

    public String decryptText(String hash) {
        return textEncryptor.decrypt(hash);
    }
}
