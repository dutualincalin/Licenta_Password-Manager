package org.PasswordManager.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import org.PasswordManager.exceptions.ExceededQRCapacityException;
import org.PasswordManager.exceptions.InternalServerErrorException;
import org.PasswordManager.exceptions.WrongPathException;
import org.PasswordManager.utility.Utils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Scanner;

@Service
public class IOService {
    private final WrongPathException wrongPathException;

    private final InternalServerErrorException internalServerErrorException;

    private final ExceededQRCapacityException exceededQRCapacityException;

    public IOService(WrongPathException wrongPathException,
                     InternalServerErrorException internalServerErrorException,
                     ExceededQRCapacityException exceededQRCapacityException) {
        this.wrongPathException = wrongPathException;
        this.internalServerErrorException = internalServerErrorException;
        this.exceededQRCapacityException = exceededQRCapacityException;
    }

    public void saveConfigToFile(String stringToWrite){
        try (FileWriter writer = new FileWriter(Utils.CONFIG_FILE_NAME)){
            writer.write(stringToWrite);
        } catch (IOException e) {
            e.printStackTrace();
            throw internalServerErrorException;
        }
    }

    public String gatherConfigFromFile(){
        String gatheredString;

        try {
            Scanner scanner = new Scanner(new File(Utils.CONFIG_FILE_NAME));
            gatheredString = scanner.nextLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw wrongPathException;
        }

        return gatheredString;
    }

    public void createQR(String text, String QRName) {
        int exceedingCapacity = text.length() - Utils.QR_MAX_STORAGE;

        if(exceedingCapacity > 0){
            exceededQRCapacityException.setExceedingCapacity(exceedingCapacity);
            throw exceededQRCapacityException;
        }

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix matrix;
        File QRCodesDirectory = new File("./QRCodes");

        if(!QRCodesDirectory.exists()) {
            try {
                if (!QRCodesDirectory.mkdir()) {
                    throw new IOException();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw internalServerErrorException;
            }
        }

        try {
            matrix = qrCodeWriter.encode(
                text,
                BarcodeFormat.QR_CODE,
                Utils.QR_CODE_SIZE,
                Utils.QR_CODE_SIZE
            );

            Path QRPath = FileSystems.getDefault().getPath("./QRCodes/" + QRName);
            MatrixToImageWriter.writeToPath(matrix, "PNG", QRPath);
        } catch (InvalidPathException e) {
            throw wrongPathException;
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            throw internalServerErrorException;
        }
    }

    public String readQR(String path) {
        Result result;
        File pathFD = new File(path);

        if(!pathFD.exists()){
            throw wrongPathException;
        }

        try {
            BufferedImage QR = ImageIO.read(pathFD);

            BinaryBitmap binaryBitmap =
                new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(QR)));

            result = new MultiFormatReader().decode(binaryBitmap);
        } catch (NotFoundException e) {
            throw wrongPathException;
        } catch (IOException e) {
            e.printStackTrace();
            throw internalServerErrorException;
        }

        return result.getText();
    }

    public byte[] getImageData(String path) {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);

            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
