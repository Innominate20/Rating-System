package com.ratingsystem.RatingSystem.controller.authenticationController;

import com.ratingsystem.RatingSystem.dto.LoginRequest;
import com.ratingsystem.RatingSystem.service.authenticationService.LoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class LoginController {
    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginMethod(@RequestBody @Valid LoginRequest loginRequest){
        return loginService.loginUser(loginRequest);
    }
}
