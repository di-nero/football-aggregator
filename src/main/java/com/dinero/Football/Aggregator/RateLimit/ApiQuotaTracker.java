package com.dinero.Football.Aggregator.RateLimit;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
public class ApiQuotaTracker {

   @Autowired
   private RedisTemplate<String , String> redisTemplate;

    String cacheKey = "api_football::calls::" + YearMonth.now();
    public boolean isSafe(){


        String count = redisTemplate.opsForValue().get(cacheKey);

        if (count == null)
            return true;
        return Integer.parseInt(count) < 90;
    }
    public void increment(){

        redisTemplate.opsForValue().increment(cacheKey);

    }
}
