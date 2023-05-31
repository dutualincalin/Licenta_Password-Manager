package org.PasswordManager.service;

import org.PasswordManager.exceptions.HashErrorException;
import org.PasswordManager.mapper.PasswordMapper;
import org.PasswordManager.model.PasswordMetadata;
import org.PasswordManager.utility.Utils;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class EncryptionService {

    private final PasswordMapper mapper;

    private final TextEncryptor textEncryptor;

    private final Pbkdf2PasswordEncoder pbkdf2PasswordEncoder;

    private final Argon2PasswordEncoder argon2PasswordEncoder;

    private final HashErrorException hashErrorException;

    public EncryptionService(HashErrorException hashErrorException){
        this.hashErrorException = hashErrorException;
        this.mapper = PasswordMapper.instance;
        String token = "reganaMdrowssaP";

        this.pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder(
                token + "pbkdf2",
                Utils.PBKDF2_HASH_ITERATIONS,
                Utils.PBKDF2_HASH_SIZE
        );

        pbkdf2PasswordEncoder.setEncodeHashAsBase64(true);

        this.argon2PasswordEncoder = new Argon2PasswordEncoder(
                Utils.SALT_SIZE,
                Utils.ARGON2_HASH_SIZE,
                Utils.THREAD_NUM,
                Utils.MEMORY,
                Utils.ARGON2_HASH_ITERATIONS
        );

        textEncryptor = Encryptors.text(
            token + "config",
            KeyGenerators.string().generateKey()
        );
    }

    public String encryptImage(BufferedImage image, String type) {
        String imageString = mapper.imageToBase64String(image, type);
        return pbkdf2PasswordEncoder.encode(imageString);
    }

    public String encryptPassword(String imgHash, PasswordMetadata passwordMetadata, String master) {
        String finalHash =  argon2PasswordEncoder.encode(
            imgHash + passwordMetadata.toString() + master
        );

        int index = finalHash.indexOf("$$");

        if(index == -1)
            throw hashErrorException;

        return finalHash.substring(index + 2);
    }

    public String encryptText(String text) {
        return textEncryptor.encrypt(text);
    }

    public String decryptText(String hash) {
        return textEncryptor.decrypt(hash);
    }
}
