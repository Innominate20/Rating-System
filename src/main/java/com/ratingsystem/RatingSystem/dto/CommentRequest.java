package com.ratingsystem.RatingSystem.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentRequest {
    @NotBlank(message = "Message not provided !")
    private String message;
    @NotNull(message = "Rating not provided !")
    @Min(0)
    @Max(10)
    private int rating;
}
