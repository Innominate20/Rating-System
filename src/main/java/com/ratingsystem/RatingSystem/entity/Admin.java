package com.ratingsystem.RatingSystem.entity;

import com.ratingsystem.RatingSystem.enums.Role;
import com.ratingsystem.RatingSystem.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "admins", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private boolean verified ;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDate created_at;
    @Enumerated(EnumType.STRING)
    private Role role;
}
