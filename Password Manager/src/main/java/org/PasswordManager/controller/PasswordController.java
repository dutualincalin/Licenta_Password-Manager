package org.PasswordManager.controller;

import org.PasswordManager.model.PasswordMetadata;
import org.PasswordManager.service.PasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping("/password")
public class PasswordController {
    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PostMapping(value = "/addPassMeta")
    public ResponseEntity<Void> addPasswordMetadata(
        @Validated @RequestBody PasswordMetadata passwordMetadata
    ) {
        passwordService.addPasswordMetadata(passwordMetadata);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/delPassMeta")
    public ResponseEntity<Void> deletePasswordMetadata(
        @Validated @RequestBody PasswordMetadata passwordMetadata
    ) {
        passwordService.removePasswordMetadata(passwordMetadata);
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/getPassMetaList")
    public ResponseEntity<ArrayList<PasswordMetadata>> getPasswordMetadataList() {
        return ResponseEntity.status(200).body(passwordService.getPasswordMetadataList());
    }

    @GetMapping("/generatePassword")
    public ResponseEntity<String> generatePassword(
        @RequestParam String masterPass,
        @Validated @RequestBody PasswordMetadata passwordMetadata
    ) {
        return ResponseEntity.status(201)
            .body(passwordService.generatePassword(masterPass, passwordMetadata));
    }
}
