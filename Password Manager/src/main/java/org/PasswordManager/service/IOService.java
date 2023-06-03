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
import org.PasswordManager.exceptions.InternalServerErrorException;
import org.PasswordManager.exceptions.WrongPathException;
import org.PasswordManager.utility.Utils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

@Service
public class IOService {
    private final WrongPathException wrongPathException;

    private final InternalServerErrorException internalServerErrorException;

    public IOService(WrongPathException wrongPathException,
                     InternalServerErrorException internalServerErrorException) {
        this.wrongPathException = wrongPathException;
        this.internalServerErrorException = internalServerErrorException;
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
            throw internalServerErrorException;
        }

        return gatheredString;
    }

    public int compareQRStrings(String QRString1, String QRString2) {
        return Integer.getInteger(QRString1.substring(
            QRString1.indexOf(" - ") + 1,
            QRString1.indexOf(" - ", QRString1.indexOf(" - "))
        )) - Integer.getInteger(QRString2.substring(
            QRString2.indexOf(" - ") + 1,
            QRString2.indexOf(" - ", QRString2.indexOf(" - "))
        ));
    }

    public void createQR(String text, String QRName) {
        int textLength = text.length();
        int qrNum = textLength / Utils.QR_MAX_STORAGE +
            ((textLength % Utils.QR_MAX_STORAGE > 0) ? 1 : 0);

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

        if(qrNum == 1) {
            try {
                matrix = qrCodeWriter
                    .encode(text, BarcodeFormat.QR_CODE, Utils.QR_CODE_SIZE, Utils.QR_CODE_SIZE);

                Path QRPath = FileSystems.getDefault().getPath("./QRCodes/" + QRName);

                MatrixToImageWriter.writeToPath(matrix, "PNG", QRPath);
            } catch (InvalidPathException e) {
                throw wrongPathException;
            } catch (WriterException | IOException e) {
                e.printStackTrace();
                throw internalServerErrorException;
            }
        }

        else {
            File QRDirectory = new File(QRName);

            try {
                if (!QRDirectory.mkdir()) {
                    throw new IOException();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw internalServerErrorException;
            }

            try {
                for (int i = 0; i < qrNum; i++) {
                    matrix = qrCodeWriter.encode(
                        QRName + " - " + (i + 1) + " - " + text,
                        BarcodeFormat.QR_CODE,
                        Utils.QR_CODE_SIZE,
                        Utils.QR_CODE_SIZE
                    );

                    Path QRPath = FileSystems.getDefault()
                        .getPath("./QRCodes/" + QRName + "/" + (i + 1));

                    MatrixToImageWriter.writeToPath(matrix, "PNG", QRPath);
                }
            } catch (InvalidPathException e) {
                throw wrongPathException;
            } catch (WriterException | IOException e) {
                e.printStackTrace();
                throw internalServerErrorException;
            }
        }
    }

    public String readQR(String path) {
        Result result;
        File pathFD = new File(path);

        if(!pathFD.exists()){
            throw wrongPathException;
        }

        else if(pathFD.isFile()) {
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

        ArrayList<String> QRStrings = new ArrayList<>();

        try {
            for (File file : Objects.requireNonNull(pathFD.listFiles())) {
                BufferedImage QR = ImageIO.read(file);

                BinaryBitmap binaryBitmap =
                    new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(QR)));

                result = new MultiFormatReader().decode(binaryBitmap);
                QRStrings.add(result.getText());
            }
        } catch (NotFoundException e) {
            throw wrongPathException;
        } catch (IOException e) {
            e.printStackTrace();
            throw internalServerErrorException;
        }

        // sort Strings
        QRStrings.sort(this::compareQRStrings);

        StringBuilder finalString = new StringBuilder();
        for(String string : QRStrings){
            finalString.append(string
                .substring(string.indexOf(" - ", string.indexOf(" - ")) + 1));
        }

        return finalString.toString();
    }
}
