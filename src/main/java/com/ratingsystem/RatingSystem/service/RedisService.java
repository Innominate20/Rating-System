package com.ratingsystem.RatingSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;
    @Autowired
    public RedisService(StringRedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public void storeToken(String token, String email) {

        redisTemplate.opsForValue().set(token,email,24, TimeUnit.HOURS);
    }

    public Optional<String>  getUserEmail(String token) {

        return Optional.ofNullable(redisTemplate.opsForValue().get(token));
    }

    public void deleteToken(String token) {
        redisTemplate.delete(token);
    }
}
