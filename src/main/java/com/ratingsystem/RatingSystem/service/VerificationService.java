package com.ratingsystem.RatingSystem.service;

import com.ratingsystem.RatingSystem.entity.User;
import com.ratingsystem.RatingSystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationService {

    private final RedisService redisService;
    private final UserRepository userRepository;
    @Autowired
    public VerificationService(RedisService redisService, UserRepository userRepository) {
        this.redisService = redisService;
        this.userRepository = userRepository;
    }

    @Transactional
    public ResponseEntity<String> verificationMethod(String token){

        Optional<String> optional = redisService.getUserEmail(token);

        if(optional.isPresent()){
            String email = optional.get();
            Optional<User> userToVerify =  userRepository.findByEmail(email);
            userToVerify.get().setVerified(true);
            redisService.deleteToken(token);
           return ResponseEntity.status(HttpStatus.ACCEPTED).body("User verified successfully !");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong token or Time expired !");
    }
}
