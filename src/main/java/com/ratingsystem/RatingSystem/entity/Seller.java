package com.ratingsystem.RatingSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ratingsystem.RatingSystem.enums.Role;
import com.ratingsystem.RatingSystem.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sellers", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class Seller{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    @JsonIgnore
    private String password;
    private String email;
    private boolean verified = false;
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<GameObject> gameObjects;
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;
    private LocalDate created_at;
    @Enumerated(EnumType.STRING)
    private Role role = Role.SELLER;
}
