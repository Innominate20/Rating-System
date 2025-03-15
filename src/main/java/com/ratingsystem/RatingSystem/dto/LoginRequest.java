package com.ratingsystem.RatingSystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {
    @NotBlank(message = "Email not provided !")
    private String email;
    @NotBlank(message = "Password not provided !")
    private String password;
}
