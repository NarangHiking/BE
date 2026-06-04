package com.naranghiking.auth.filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.naranghiking.auth.service.TokenService;
import com.naranghiking.common.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {
	
	@Mock
	private JwtUtil jwtUtil;
	
	@Mock
    private TokenService tokenService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtFilter jwtFilter;
    
    @Test
    @DisplayName("토큰 없으면 다음 필터로")
    void noToken() throws Exception {
    	when(request.getHeader("Authorization")).thenReturn(null);
    	
    	jwtFilter.doFilterInternal(request, response, filterChain);
    	
    	verify(filterChain).doFilter(request, response);
    }

    
    @Test
    @DisplayName("블랙리스트 토큰 차단")
    void blacklistedToken() throws Exception {
    	when(request.getHeader("Authorization")).thenReturn("Bearer accessToken");
    	when(tokenService.isBlacklisted("accessToken")).thenReturn(true);
    	
    	jwtFilter.doFilterInternal(request, response, filterChain);
    	
    	verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    	verify(filterChain, never()).doFilter(request, response);
    }
    
    @Test
    void expiredToken() throws Exception {
    	when(request.getHeader("Authorization")).thenReturn("Bearer accessToken");
    	when(tokenService.isBlacklisted("accessToken")).thenReturn(false);
    	when(jwtUtil.isExpired("accessToken")).thenReturn(true);
    	
    	jwtFilter.doFilterInternal(request, response, filterChain);
    	
    	verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    	verify(filterChain, never()).doFilter(request, response);
    }
    
    
    @Test
    @DisplayName("정상 토큰 통과")
    void validToken() throws Exception {
    	when(request.getHeader("Authorization")).thenReturn("Bearer accessToken");
    	when(tokenService.isBlacklisted("accessToken")).thenReturn(false);
    	when(jwtUtil.isExpired("accessToken")).thenReturn(false);
    	when(jwtUtil.getUserId("accessToken")).thenReturn(123L);
    	
    	jwtFilter.doFilterInternal(request, response, filterChain);
    	
    	verify(filterChain).doFilter(request, response);
    }
}
