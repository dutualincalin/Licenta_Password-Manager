package org.PasswordManager.mapper;

import org.PasswordManager.utility.Utils;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PasswordMapper {


    default String imageToBase64String(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            imageString = Base64.getEncoder().encodeToString(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    default String pngImageToHash(BufferedImage image) {
        String imageString = imageToBase64String(image, "png");

        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder =  new Pbkdf2PasswordEncoder(
            "reganaMdrowssaP",
            Utils.pbkdf2Iterations,
            Utils.pbkdf2Length
        );

        pbkdf2PasswordEncoder.setEncodeHashAsBase64(true);
        return pbkdf2PasswordEncoder.encode(imageString);
    }
}
