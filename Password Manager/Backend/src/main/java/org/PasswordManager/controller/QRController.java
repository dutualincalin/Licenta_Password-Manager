package org.PasswordManager.controller;

import org.PasswordManager.model.PasswordMetadata;
import org.PasswordManager.service.ConfigurationService;
import org.PasswordManager.service.PasswordService;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/qr")
public class QRController {
    private final ConfigurationService configurationService;

    private final PasswordService passwordService;

    public QRController(ConfigurationService configurationService, PasswordService passwordService) {
        this.configurationService = configurationService;
        this.passwordService = passwordService;
    }

    @PostMapping("/exportQR")
    public ResponseEntity<byte[]> exportConfigToQR(
        @Validated @RequestBody ArrayList<PasswordMetadata> passwordMetadataList
    ) {
        byte[] imageBytes = configurationService.exportConfigToQR(passwordMetadataList);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(imageBytes.length);

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/readQR")
    public ResponseEntity<Void> readConfigFromQR(@RequestParam String path) {
        passwordService.addPasswordsToMetadataList(configurationService.readConfigFromQR(path));
        return ResponseEntity.status(200).build();
    }
}
