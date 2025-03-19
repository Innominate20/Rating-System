package com.ratingsystem.RatingSystem.service.authenticationService;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PasswordResetServiceTest {

    @Autowired
    private RedisService redisService;

    @Autowired
    private PasswordResetService passwordResetService;

    @Test
    void testPasswordResetService(){

        String email = "test@gmail.com";
        String token = "ewjnwprvn99894998hev98";

        redisService.storeToken(token,email);

        ResponseEntity<?> response = passwordResetService.validateToken(token);

        redisService.deleteToken(token);

        assertEquals(HttpStatus.OK, response.getStatusCode(),"Status codes should be the same !");
    }
}
