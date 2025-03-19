package com.ratingsystem.RatingSystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PasswordResetRequest {
    @NotBlank(message = "Token not provided !")
    private String token;
    @NotBlank(message = "Password not provided !")
    private String password;
}
