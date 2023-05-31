package org.PasswordManager.controller;

import org.PasswordManager.model.PasswordMetadata;
import org.PasswordManager.service.ConfigurationService;
import org.PasswordManager.service.PasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/config")
public class ConfigurationController {
    private final ConfigurationService configurationService;

    private final PasswordService passwordService;

    public ConfigurationController(ConfigurationService configurationService,
                                   PasswordService passwordService) {
        this.configurationService = configurationService;
        this.passwordService = passwordService;
    }

    @PostMapping("/imgConfig")
    public ResponseEntity<Void> setImageConfiguration(@RequestParam String imgPath) {
        configurationService.setConfigurationImage(imgPath);
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/ExportQR")
    public ResponseEntity<String> exportConfigToQR(@RequestBody ArrayList<PasswordMetadata> passwordMetadataList) {
        return ResponseEntity.status(201)
            .body(configurationService.exportConfigToQR(passwordMetadataList));
    }

    @PutMapping("/ReadQR")
    public ResponseEntity<Void> readConfigFromQR(@RequestParam String path) {
        passwordService.setPasswordMetadataList(configurationService.readConfigFromQR(path));
        return ResponseEntity.status(200).build();
    }

    @PutMapping("/ConfigSave")
    public  ResponseEntity<Void> saveAppConfig() {
        configurationService.saveConfiguration(passwordService.getPasswordMetadataList());
        return ResponseEntity.status(200).build();
    }
}
