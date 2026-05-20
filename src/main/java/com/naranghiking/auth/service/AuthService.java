package com.naranghiking.auth.service;

import com.naranghiking.auth.dto.LoginRequest;
import com.naranghiking.common.util.JwtUtil;
import com.naranghiking.user.service.UserService;
import com.naranghiking.user.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final TokenService tokenService;
    private JwtUtil jwtUtil;

    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userService.findByEmail(request.getEmail());
        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("login fail");
        if (!userService.checkPassword(request.getPassword(), user.getPassword()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login fail");

        String accessToken =jwtUtil.generateAccessToken(String.valueOf(user.getUserId()));
        String refreshToken =jwtUtil.generateAccessToken(String.valueOf(user.getUserId()));
        tokenService.saveRefreshToken(String.valueOf(user.getUserId()), refreshToken);

        return ResponseEntity.ok(Map.of("accessToken", accessToken, "refreshToken",refreshToken));
    }

    public void logout(String userId, String accessToken) {
        tokenService.blackListAccessToken(accessToken);
        tokenService.deleteRefreshToken(userId);
    }

    public String reissue(String userId, String refreshToken) {
        String saved = tokenService.getRefreshToken(userId);
        if (saved == null || !saved.equals(refreshToken)) {
            throw new RuntimeException("유효하지 않은 Refresh Token");
        }
        return jwtUtil.generateAccessToken(userId);
    }
}
