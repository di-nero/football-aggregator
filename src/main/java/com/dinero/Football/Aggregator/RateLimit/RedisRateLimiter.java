package com.dinero.Football.Aggregator.RateLimit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.TimeUnit;


@Component
public class RedisRateLimiter{

    @Autowired
    private RedisTemplate<String , String> redisTemplate;

    public boolean isAllowed(String userId){

        long currentMinute = Instant.now().getEpochSecond() / 60;
        String cachedKey = "RateLimit::" + userId + "::" + currentMinute;

        long count = redisTemplate.opsForValue().increment(cachedKey);

        if (count == 1){
            redisTemplate.expire(cachedKey , 70 , TimeUnit.SECONDS);
        }
        return count <= 60;
    }

}