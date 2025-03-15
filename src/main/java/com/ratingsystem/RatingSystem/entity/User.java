package com.ratingsystem.RatingSystem.entity;

import com.ratingsystem.RatingSystem.enums.Status;
import jakarta.persistence.*;
import com.ratingsystem.RatingSystem.enums.Role;
import lombok.*;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.Date;
import java.util.Enumeration;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private boolean verified = false;
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;
    private LocalDate created_at;
    @Enumerated(EnumType.STRING)
    private Role role = Role.SELLER;

}
