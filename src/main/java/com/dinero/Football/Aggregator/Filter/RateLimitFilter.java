package com.dinero.Football.Aggregator.Filter;

import com.dinero.Football.Aggregator.RateLimit.RedisRateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.ServletException;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    @Autowired
    private RedisRateLimiter redisRateLimiter;

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws ServletException, IOException {
        String userId = request.getHeader("X-User-Id");

       if (redisRateLimiter.isAllowed(userId))
           filterChain.doFilter(request , response);
       else{
           response.setStatus(429);
           response.getWriter().write("Rate limit exceeded. Try again in 60 seconds.");
       }
    }

}
