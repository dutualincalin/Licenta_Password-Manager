package org.PasswordManager.controller;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.PasswordManager.model.PasswordConfiguration;
import org.PasswordManager.service.PasswordService;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.ArrayList;

@RestController
@RequestMapping("/password")
public class PasswordController {
    private final PasswordService passwordService;

    private final Bucket bucket;

    public PasswordController(PasswordService passwordService) {
        Bandwidth limit = Bandwidth.classic(20, Refill
            .greedy(20, Duration.ofMinutes(1))
        );

        this.bucket = Bucket4j.builder().addLimit(limit).build();

        this.passwordService = passwordService;
    }


    /**
     ** Password configuration endpoints
     ************************************************************************************/

    @PostMapping(value = "/addPassMeta")
    public ResponseEntity<Void> createPasswordMetadata(
        @Validated @RequestBody PasswordConfiguration passwordConfiguration
    ) {
        passwordService.addPasswordConfiguration(passwordConfiguration);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/delPassMeta")
    public ResponseEntity<Void> deletePasswordMetadata(
        @RequestParam String id
    ) {
        passwordService.removePasswordConfiguration(id);
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/getPassMetaList")
    public ResponseEntity<ArrayList<PasswordConfiguration>> getPasswordMetadataList() {
        return ResponseEntity.status(200).body(passwordService.getPasswordConfigurationList());
    }


    /**
     ** Password generation endpoint
     ************************************************************************************/

    @GetMapping("/generatePassword")
    public ResponseEntity<String> generatePassword(
        @RequestParam String masterPass,
        @RequestParam String id
    ) {
        if (bucket.tryConsume(1)) {
            return ResponseEntity.status(201).body(new JSONObject()
                .put("passKey", passwordService.generatePassword(masterPass, id))
                .toString()
            );
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}
