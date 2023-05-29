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
import org.PasswordManager.utility.Utils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Scanner;

@Service
public class IOService {
    public void saveConfigToFile(String stringToWrite){
        try (FileWriter writer = new FileWriter(Utils.CONFIG_FILE_NAME)){
            writer.write(stringToWrite);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String gatherConfigFromFile(){
        String gatheredString;

        try {
            Scanner scanner = new Scanner(new File(Utils.CONFIG_FILE_NAME));
            gatheredString = scanner.nextLine();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return gatheredString;
    }

    public void createQR(String text, String QRName) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        BitMatrix matrix;
        try {
            matrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, Utils.QR_CODE_SIZE, Utils.QR_CODE_SIZE);
            File QRDirectory = new File("./QRCodes");
            if(!QRDirectory.mkdir()) {
                throw new IOException();
            }

            Path QRPath = FileSystems.getDefault().getPath("./QRCodes/" + QRName + ".png");
            MatrixToImageWriter.writeToPath(matrix, "PNG", QRPath);
        } catch (WriterException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readQR(String path) {
        Result result;

        try {
            BufferedImage QR = ImageIO.read(new FileInputStream(path));

            BinaryBitmap binaryBitmap =
                new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(QR)));

            result = new MultiFormatReader().decode(binaryBitmap);
        } catch (IOException | NotFoundException e) {
            throw new RuntimeException(e);
        }

        return result.getText();
    }
}
