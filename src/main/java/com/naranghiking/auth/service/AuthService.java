package com.naranghiking.auth.service;

import com.naranghiking.auth.dto.LoginRequest;
import com.naranghiking.auth.dto.TokenResponse;
import com.naranghiking.common.exception.InvalidTokenException;
import com.naranghiking.common.exception.TokenExpiredException;
import com.naranghiking.common.exception.UserNotFoundException;
import com.naranghiking.common.util.JwtUtil;
import com.naranghiking.user.service.UserService;
import com.naranghiking.user.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final TokenService tokenService;
    private final JwtUtil jwtUtil;

    public TokenResponse login(LoginRequest request) {
        User user = userService.findByEmail(request.getEmail());
        
        if (user == null)
            throw new UserNotFoundException("사용자를 찾을 수 없습니다");
        if (!userService.checkPassword(request.getPassword(), user.getPassword()))
        	throw new BadCredentialsException("401_UNAUTHORIZED");

        String accessToken =jwtUtil.generateAccessToken(String.valueOf(user.getUserId()));
        String refreshToken =jwtUtil.generateRefreshToken(String.valueOf(user.getUserId()));
        tokenService.saveRefreshToken(String.valueOf(user.getUserId()), refreshToken);
        
        return new TokenResponse(accessToken, refreshToken, user.getUserId());
    }

    public void logout(String userId, String accessToken) {
        tokenService.blacklistAccessToken(accessToken);
        tokenService.deleteRefreshToken(userId);
    }

    public TokenResponse reissue(String refreshToken) {
    	if (jwtUtil.isExpired(refreshToken)) {
    		throw new TokenExpiredException("refresh token 만료");
    	}
    	
    	String userId = jwtUtil.getUserId(refreshToken);
        String saved = tokenService.getRefreshToken(userId);
        if (saved == null || !saved.equals(refreshToken)) {
            throw new InvalidTokenException("유효하지 않은 Refresh Token");
        }
        String newAccessToken = jwtUtil.generateAccessToken(userId);
        return new TokenResponse(newAccessToken, refreshToken, userId);
    }
}
