package org.PasswordManager.controller;

import org.PasswordManager.model.PasswordMetadata;
import org.PasswordManager.service.ConfigurationService;
import org.PasswordManager.service.PasswordService;
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
@RequestMapping("/config")
public class ConfigurationController {
    private final ConfigurationService configurationService;

    private final PasswordService passwordService;

    public ConfigurationController(ConfigurationService configurationService,
                                   PasswordService passwordService) {
        this.configurationService = configurationService;
        this.passwordService = passwordService;
    }

    @GetMapping("/imgConfig")
    public ResponseEntity<Void> setImageConfiguration(@RequestParam String imgPath) {
        configurationService.setConfigurationImage(imgPath);
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/exportQR")
    public ResponseEntity<String> exportConfigToQR(
        @Validated @RequestBody() ArrayList<PasswordMetadata> passwordMetadataList
    ) {
        return ResponseEntity.status(201).body(
            configurationService.exportConfigToQR(passwordMetadataList));
    }

    @GetMapping("/readQR")
    public ResponseEntity<Void> readConfigFromQR(@RequestParam String path) {
        passwordService.addPasswordsToMetadataList(configurationService.readConfigFromQR(path));
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/configSave")
    public ResponseEntity<Void> saveAppConfig() {
        configurationService.saveConfiguration(passwordService.getPasswordMetadataList());
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/configGather")
    public ResponseEntity<Void> gatherAppConfig() {
        passwordService.setPasswordMetadataList(configurationService.gatherConfiguration());
        return ResponseEntity.status(200).build();
    }
}
