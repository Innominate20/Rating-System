package com.ratingsystem.RatingSystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class GameObjectRequest {
    @NotBlank(message = "Title not provided !")
    private String title;
    private String text;
}
