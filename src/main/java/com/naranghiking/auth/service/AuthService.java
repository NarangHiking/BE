package com.naranghiking.auth.service;

import com.naranghiking.auth.dto.LoginRequest;
import com.naranghiking.auth.dto.TokenResponse;
import com.naranghiking.common.exception.InvalidTokenException;
import com.naranghiking.common.exception.TokenExpiredException;
import com.naranghiking.common.exception.UserNotFoundException;
import com.naranghiking.common.util.JwtUtil;
import com.naranghiking.user.service.UserService;
import com.naranghiking.user.service.UserServiceImpl;
import com.naranghiking.user.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userServiceImpl;
    private final TokenService tokenService;
    private final JwtUtil jwtUtil;

    public TokenResponse login(LoginRequest request) {

        User user = userServiceImpl.findByEmail(request.getEmail());
        if (user == null)
            throw new UserNotFoundException("사용자를 찾을 수 없습니다");

        System.out.println("rawPassword: " + request.getPass());
        System.out.println("encodedPassword: " + user.getPass());
        System.out.println("matches: " + userServiceImpl.checkPassword(request.getPass(), user.getPass()));

        if (!userServiceImpl.checkPassword(request.getPass(), user.getPass()))
        	throw new BadCredentialsException("401_UNAUTHORIZED");

        String accessToken =jwtUtil.generateAccessToken(String.valueOf(user.getId()));
        String refreshToken =jwtUtil.generateRefreshToken(String.valueOf(user.getId()));
        tokenService.saveRefreshToken(String.valueOf(user.getId()), refreshToken);
        return new TokenResponse(accessToken, refreshToken, user.getId().toString());
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
