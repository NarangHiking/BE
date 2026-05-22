package com.naranghiking.auth.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
		// TODO Auto-generated method stub
		String token = resolveToken(request);
		
		// 토큰이 없는 경우 다음 필터로 => 토큰이 없는 경우는 오류가 아니며 발급이 안된 상태를 의미함.
		if (token == null) {
			filterChain.doFilter(request, response);
			return ;
		}
		
		// 블랙리스트 확인 || 만료 확인
		if (tokenService.isBlacklisted(token) || jwtUtil.isExpired(token)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return ;
		}
		
		String userId= jwtUtil.getUserId(token);
		UsernamePasswordAuthenticationToken authentication = 
				new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		filterChain.doFilter(request, response);
	}
	
	private String resolveToken(HttpServletRequest request) {
		String bearer = request.getHeader("Authorization");
		if (bearer != null && bearer.startsWith("Bearer ")) {
			return bearer.substring(7);
		}
		return null;
	}
}
