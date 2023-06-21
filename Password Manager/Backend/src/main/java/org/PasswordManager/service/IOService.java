package org.PasswordManager.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.PasswordManager.exceptions.ExceededQRCapacityException;
import org.PasswordManager.exceptions.InternalServerException;
import org.PasswordManager.exceptions.app.MissingConfigurationException;
import org.PasswordManager.utility.Utils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

@Service
public class IOService {
    private final MissingConfigurationException missingConfigurationException;
    private final ExceededQRCapacityException exceededQRCapacityException;
    private final InternalServerException internalServerException;


    public IOService(MissingConfigurationException missingConfigurationException,
                     InternalServerException internalServerException,
                     ExceededQRCapacityException exceededQRCapacityException) {
        this.missingConfigurationException = missingConfigurationException;
        this.internalServerException = internalServerException;
        this.exceededQRCapacityException = exceededQRCapacityException;
    }


    /**
     ** Configuration File methods
     ************************************************************************************/

    public void saveConfigToFile(String stringToWrite){
        try (FileWriter writer = new FileWriter(Utils.CONFIG_FILE_NAME)){
            writer.write(stringToWrite);
        } catch (IOException e) {
            e.printStackTrace();
            throw internalServerException;
        }
    }

    public String gatherConfigFromFile(){
        try {
            Scanner scanner = new Scanner(new File(Utils.CONFIG_FILE_NAME));
            return scanner.nextLine();
        } catch (FileNotFoundException e) {
            throw missingConfigurationException;
        }
    }


    /**
     ** QR method
     ************************************************************************************/

    public byte[] createQR(String text) {
        int exceedingCapacity = text.length() - Utils.QR_MAX_STORAGE;

        if(exceedingCapacity > 0){
            exceededQRCapacityException.setExceedingCapacity(exceedingCapacity);
            throw exceededQRCapacityException;
        }

        try {
            BitMatrix matrix = new QRCodeWriter()
                .encode(text, BarcodeFormat.QR_CODE, Utils.QR_CODE_SIZE, Utils.QR_CODE_SIZE);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(
                MatrixToImageWriter.toBufferedImage(matrix),
                "png",
                byteArrayOutputStream
            );

            return byteArrayOutputStream.toByteArray();
        } catch (WriterException | IOException e) {
            throw internalServerException;
        }
    }
}
