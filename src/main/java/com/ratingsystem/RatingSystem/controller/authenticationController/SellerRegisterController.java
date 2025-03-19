package com.ratingsystem.RatingSystem.controller.authenticationController;

import com.ratingsystem.RatingSystem.dto.UserRegisterRequest;
import com.ratingsystem.RatingSystem.service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class SellerRegisterController {

    private final RegisterService registerService;
    @Autowired
    public SellerRegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerMethod(@Valid @RequestBody UserRegisterRequest userRegisterRequest){
        return registerService.registerSeller(userRegisterRequest);

    }

}
