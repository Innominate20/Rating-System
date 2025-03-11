package com.ratingsystem.RatingSystem.service;

import com.ratingsystem.RatingSystem.dto.UserRegisterRequest;
import com.ratingsystem.RatingSystem.entity.User;
import com.ratingsystem.RatingSystem.enums.Status;
import com.ratingsystem.RatingSystem.repository.UserRepository;
import com.ratingsystem.RatingSystem.util.TokenGenerator;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;
import java.util.Date;

@Service
public class RegisterService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final RedisService redisService;
    @Autowired
    public RegisterService(UserRepository userRepository,BCryptPasswordEncoder passwordEncoder,
                           EmailService emailService,RedisService redisService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisService = redisService;
        this.emailService = emailService;
    }

    public ResponseEntity<String> registerUser(UserRegisterRequest user) {
        User newUser = User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .created_at(LocalDate.now())
                .build();
        userRepository.save(newUser);

        String token = TokenGenerator.generateToken();
        redisService.storeToken(token,user.getEmail());

        try {
            emailService.sendVerificationCode(user.getEmail(),token);
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending verification code !");
        }
        return  ResponseEntity.status(HttpStatus.CREATED).body("User created waiting verification !");

    }
}
