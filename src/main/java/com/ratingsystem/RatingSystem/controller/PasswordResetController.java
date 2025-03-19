package com.ratingsystem.RatingSystem.controller;

import com.ratingsystem.RatingSystem.dto.PasswordResetRequest;
import com.ratingsystem.RatingSystem.service.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/forgotPassword/{email}")
    public ResponseEntity<?> getResetCode(@PathVariable("email")String email){

        return passwordResetService.sendPasswordResetCode(email);
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest){

        return passwordResetService.resetPassword(passwordResetRequest.getToken(), passwordResetRequest.getPassword());
    }

    @GetMapping("/checkToken/{token}")
    public ResponseEntity<?> validateToken(@PathVariable("token") String token){

        return passwordResetService.validateToken(token);
    }
}
