package org.PasswordManager.controller;

import lombok.extern.slf4j.Slf4j;
import org.PasswordManager.service.ConfigurationService;
import org.PasswordManager.service.PasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/config")
public class ConfigurationController {
    private final ConfigurationService configurationService;

    private final PasswordService passwordService;

    public ConfigurationController(
        ConfigurationService configurationService,
        PasswordService passwordService
    ) {
        this.configurationService = configurationService;
        this.passwordService = passwordService;
    }

    /**
     ** CSRF protocol endpoint
     ************************************************************************************/

    @GetMapping("/getCSRF")
    public ResponseEntity<Void> getCSRFToken() {
        return ResponseEntity.status(201).build();
    }


    /**
     ** App configuration endpoints
     ************************************************************************************/

    @PostMapping("/imgConfig")
    public ResponseEntity<Void> setImageConfiguration(
        @Validated @RequestBody Map<String, String> payload
    ) {

        String imgData = payload.get("image");
        configurationService.setConfigurationImage(imgData);
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/configSave")
    public ResponseEntity<Void> saveAppConfig() {
        configurationService.saveConfiguration(passwordService.getPasswordConfigurationList());
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/configGather")
    public ResponseEntity<Void> gatherAppConfig() {
        passwordService.setPasswordConfigurationList(configurationService.gatherConfiguration());
        return ResponseEntity.status(200).build();
    }
}
