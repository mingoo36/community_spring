package org.covy.mingoocommunityspring.user.service;

import org.covy.mingoocommunityspring.config.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class LogoutService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    public LogoutService(
            @Qualifier("redisTemplate") RedisTemplate<String, String> redisTemplate,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.redisTemplate = redisTemplate;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void logout(String token) {
        if (jwtTokenProvider.validateToken(token)) {
            Date expirationDate = jwtTokenProvider.getExpiration(token);
            long ttl = (expirationDate.getTime() - System.currentTimeMillis()) / 1000;
            redisTemplate.opsForValue().set(token, "logout", ttl, TimeUnit.SECONDS);

        }
    }

    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }

}
