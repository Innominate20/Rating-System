package com.ratingsystem.RatingSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;
    @Autowired
    public RedisService(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void storeToken(String token, String email) {
        stringRedisTemplate.opsForValue().set(token,email,24, TimeUnit.HOURS);
    }

    public String getUserEmail(String token) {
        return stringRedisTemplate.opsForValue().get(token);
    }

    public void deleteToken(String token) {
        stringRedisTemplate.delete(token);
    }
}
