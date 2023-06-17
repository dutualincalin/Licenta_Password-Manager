package org.PasswordManager.controller;

import org.PasswordManager.model.PasswordConfiguration;
import org.PasswordManager.service.ConfigurationService;
import org.PasswordManager.service.PasswordService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/qr")
public class QRController {
    private final PasswordService passwordService;
    private final ConfigurationService configurationService;

    public QRController(ConfigurationService configurationService, PasswordService passwordService) {
        this.passwordService = passwordService;
        this.configurationService = configurationService;
    }


    /**
     ** QR endpoints
     ************************************************************************************/

    @PostMapping("/exportQR")
    public ResponseEntity<byte[]> exportConfigToQR(
        @Validated @RequestBody ArrayList<PasswordConfiguration> passwordConfigurationList
    ) {
        byte[] imageBytes = configurationService.exportConfigToQR(passwordConfigurationList);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(imageBytes.length);

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    @PostMapping("/readQR")
    public ResponseEntity<Void> readConfigFromQR(@Validated @RequestBody String qrData) {
        passwordService.addPasswordsToConfigurationList(configurationService.readConfigFromQR(qrData));
        return ResponseEntity.status(200).build();
    }
}
