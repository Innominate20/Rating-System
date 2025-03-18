package com.ratingsystem.RatingSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ratingsystem.RatingSystem.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "comments")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String message;
    private UUID author_id;
    @ManyToOne
    @JoinColumn(name = "seller_id")
    @JsonIgnore
    private Seller seller;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDate created_at;
    @Min(0)
    @Max(10)
    private int rating;
}
