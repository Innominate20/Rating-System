package com.ratingsystem.RatingSystem.util;

import java.util.UUID;
//Utility class for generating token(UUID)
public class TokenGenerator {

    public static String generateToken(){
        return UUID.randomUUID().toString();
    }
}
