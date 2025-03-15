package com.ratingsystem.RatingSystem.service;

import com.ratingsystem.RatingSystem.dto.LoginRequest;
import com.ratingsystem.RatingSystem.entity.User;
import com.ratingsystem.RatingSystem.jwt.JwtUtility;
import com.ratingsystem.RatingSystem.repository.UserRepository;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {
    private final UserRepository userRepository;
    private final JwtUtility jwtUtility;
    private final AuthenticationManager authenticationManager;
    @Autowired
    public LoginService(UserRepository userRepository, JwtUtility jwtUtility, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtUtility = jwtUtility;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<String> loginUser(LoginRequest loginRequest){
        Optional<User> optional = userRepository.findByEmail(loginRequest.getEmail());
        if (optional.isEmpty()){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found !");
        }

        User tmpUser = optional.get();
        if (!tmpUser.isVerified()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User needs verification !");
        }

        Authentication authenticatio = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword())
        );
        UserDetails userDetails  = (UserDetails) authenticatio.getPrincipal();

        String token = jwtUtility.generateToke(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(token);
    }

}
