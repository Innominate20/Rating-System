package com.ratingsystem.RatingSystem.controller;

import com.ratingsystem.RatingSystem.dto.UserRegisterRequest;
import com.ratingsystem.RatingSystem.service.AdminService;
import com.ratingsystem.RatingSystem.service.RegisterService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final RegisterService registerService;
    private final AdminService adminService;

    @Autowired
    public AdminController(RegisterService registerService, AdminService adminService) {
        this.registerService = registerService;
        this.adminService = adminService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerAdmin(@Valid @RequestBody UserRegisterRequest userRegisterRequest){
       return registerService.registerAdmin(userRegisterRequest);
    }

    @PostMapping("/users/approve/{email}")
    public ResponseEntity<String> approveUser(@PathVariable("email") String email){
        return adminService.approveUser(email);
    }

}
