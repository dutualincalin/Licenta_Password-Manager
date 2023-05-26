package org.PasswordManager.service;

import org.PasswordManager.exceptions.HashErrorException;
import org.PasswordManager.mapper.PasswordMapper;
import org.PasswordManager.model.PasswordParams;
import org.PasswordManager.utility.Utils;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class EncryptionService {
    private final PasswordMapper mapper;

    private final Pbkdf2PasswordEncoder pbkdf2PasswordEncoder;

    private final Argon2PasswordEncoder argon2PasswordEncoder;

    private final HashErrorException hashErrorException;

    public EncryptionService(HashErrorException hashErrorException){
        this.hashErrorException = hashErrorException;
        this.mapper = PasswordMapper.instance;

        this.pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder(
                "reganaMdrowssaP",
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
    }

    public String encryptImage(BufferedImage image, String type) {
        String imageString = mapper.imageToBase64String(image, type);
        return pbkdf2PasswordEncoder.encode(imageString);
    }

    public String encryptObject(PasswordParams passwordParams) {
        String finalHash =  argon2PasswordEncoder.encode(passwordParams.toString());
        int index = finalHash.indexOf("$$");

        if(index == -1)
            throw hashErrorException;

        return finalHash.substring(index + 2);
    }
}
