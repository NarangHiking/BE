package com.naranghiking.auth.service;

import com.naranghiking.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    public void saveRefreshToken(String userId, String refreshToken) {
        long expiration = jwtUtil.getRemainExpiration(refreshToken);
        redisTemplate.opsForValue()
                .set("RT:"+userId, refreshToken, expiration, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(String userId) {
        return redisTemplate.opsForValue().get("RT:"+userId);
    }

    public void deleteRefreshToken(String userId) {
        redisTemplate.delete("RT:"+userId);
    }

    public void blackListAccessToken(String accessToken) {
        long expiration = jwtUtil.getRemainExpiration(accessToken);
        redisTemplate.opsForValue()
                .set("BL:"+accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
    }

    public boolean isBlacklisted(String accessToken) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("BL:"+accessToken));
    }
}
