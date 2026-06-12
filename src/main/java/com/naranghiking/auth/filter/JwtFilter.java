package com.naranghiking.auth.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.http.Cookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.naranghiking.auth.service.TokenService;
import com.naranghiking.common.util.JwtUtil;

import io.jsonwebtoken.lang.Collections;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	
	private final JwtUtil jwtUtil;
	private final TokenService tokenService;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = resolveToken(request);
        System.out.println("URI: " + request.getRequestURI());
        System.out.println("Token: " + token);
		
		// 토큰이 없는 경우 다음 필터로 => 토큰이 없는 경우는 오류가 아니며 발급이 안된 상태를 의미함.
		if (token == null) {
            System.out.println("토큰 없음, 통과");
			filterChain.doFilter(request, response);
			return ;
		}
		
		// 블랙리스트 확인 || 만료 확인
        try {
            if (tokenService.isBlacklisted(token) || jwtUtil.isExpired(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } catch (Exception e) {
            // 파싱 자체가 실패한 경우 (RS256, 변조된 토큰 등)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
		
		Long userId= jwtUtil.getUserId(token);
        String role = jwtUtil.getRole(token);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));


        // JWT 토큰에서 userID 추출 => Spring Security에 등록,
		UsernamePasswordAuthenticationToken authentication = 
				new UsernamePasswordAuthenticationToken(userId, null, authorities);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		filterChain.doFilter(request, response);
	}
	
	private String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        return Arrays.stream(cookies)
                .filter(c -> "accessToken".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
	}
}
