package com.ratingsystem.RatingSystem.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TokenGeneratorUtilTest {

    @Test
    void testUuidGenerator(){

        String token = TokenGeneratorUtil.generateToken();

        assertNotNull(token, "Token should not be null !");
        assertFalse(token.isEmpty(), "Token should not be empty");

        String token2 = TokenGeneratorUtil.generateToken();

        assertNotEquals(token2,token, "Tokens should be distinct !");

    }
}
