package org.PasswordManager.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

@Service
public class QRService {
    public static void createQR(String text, int height, int width, String QRName)
        throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        BitMatrix matrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        File QRDirectory = new File("./QRCodes");
        if(!QRDirectory.mkdir()) {
            // add exception here
        }

        Path QRPath = FileSystems.getDefault().getPath("./QRCodes/" + QRName);
        MatrixToImageWriter.writeToPath(matrix, "PNG", QRPath);
    }
}
