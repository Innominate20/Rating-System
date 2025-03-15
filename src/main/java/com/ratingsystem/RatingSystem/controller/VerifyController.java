package com.ratingsystem.RatingSystem.controller;

import com.ratingsystem.RatingSystem.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class VerifyController {
    private final VerificationService verificationService;

    @Autowired
    public VerifyController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }


    @PostMapping("/verify/{token}")
    public ResponseEntity<String> verifyUser(@PathVariable("token") String code){

        return verificationService.verificationMethod(code);
    }
}
