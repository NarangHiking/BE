package com.naranghiking.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.naranghiking.common.util.JwtUtil;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private TokenService tokenService;


    @Test
    @DisplayName("Refresh Token 저장")
    void saveRefreshToken() {
    	when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(jwtUtil.getRemainExpiration(anyString())).thenReturn(604800000L);
        tokenService.saveRefreshToken("123", "refreshToken");
        verify(valueOperations).set("RT:123", "refreshToken", 604800000L, TimeUnit.MILLISECONDS);
    }

    @Test
    @DisplayName("Refresh Token 조회")
    void getRefreshToken() {
    	when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("RT:123")).thenReturn("refreshToken");
        // verify(redisTemplate).delete는 메서드가 2개라서 (String)을 명시해주어야 함
        // .delete(String)
        // .delete(Collection<String>)
        assertEquals("refreshToken", tokenService.getRefreshToken((String)"123"));
    }

    @Test
    @DisplayName("Refresh Token 삭제")
    void deleteRefreshToken() {
        tokenService.deleteRefreshToken("123");
        verify(redisTemplate).delete(eq("RT:123"));
    }

    @Test
    @DisplayName("블랙리스트 등록")
    void blacklistAccessToken() {
    	when(redisTemplate.opsForValue()).thenReturn(valueOperations); 
        when(jwtUtil.getRemainExpiration(anyString())).thenReturn(1800000L);
        tokenService.blacklistAccessToken("accessToken");
        verify(valueOperations).set("BL:accessToken", "logout", 1800000L, TimeUnit.MILLISECONDS);
    }

    @Test
    @DisplayName("블랙리스트 확인 - 있음")
    void isBlacklisted_true() {
        when(redisTemplate.hasKey("BL:accessToken")).thenReturn(true);
        assertTrue(tokenService.isBlacklisted("accessToken"));
    }

    @Test
    @DisplayName("블랙리스트 확인 - 없음")
    void isBlacklisted_false() {
        when(redisTemplate.hasKey("BL:accessToken")).thenReturn(false);
        assertFalse(tokenService.isBlacklisted("accessToken"));
    }
}