package com.naranghiking.auth.service;

import com.naranghiking.auth.dto.LoginRequest;
import com.naranghiking.auth.dto.TokenResponse;
import com.naranghiking.common.exception.UserNotFoundException;
import com.naranghiking.common.util.JwtUtil;
import com.naranghiking.user.dto.SignUpRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceIntegrationTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenService tokenService;

    @Test
    void register() {
        //given
        SignUpRequest request = new SignUpRequest("a@a.com", "ssafy", "Hong");
        //when

    }

    @Test
    void login_success() {
        // given
        LoginRequest request = new LoginRequest("hong@test.com", "pass1234");

        // when
        TokenResponse response = authService.login(request);

        // then
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());

        Long userId = jwtUtil.getUserId(response.getAccessToken());
        assertEquals(2L, userId);
    }

    @Test
    void login_userNotFound() {
        //given
        LoginRequest request = new LoginRequest("notfound@test.com", "pass1234");

        //when
        assertThrows(UserNotFoundException.class, () -> authService.login(request));

    }


    @Test
    void login_wrongPassword() {
        //given
        LoginRequest request = new LoginRequest("hong@test.com", "wrongpassword");
        // when
        assertThrows(BadCredentialsException.class, () -> authService.login(request));
    }

    @Test
    void reissue_success() {
        // given
        LoginRequest request = new LoginRequest("hong@test.com", "pass1234");
        TokenResponse loginResponse = authService.login(request);
        String refreshToken = loginResponse.getRefreshToken(); // 진짜 JWT

        // when
        TokenResponse result = authService.reissue(refreshToken);

        // then
        assertNotNull(result.getAccessToken());
        assertEquals(2L, jwtUtil.getUserId(result.getAccessToken())); // 새 토큰 발급됐는지
        assertEquals(refreshToken, result.getRefreshToken()); // refresh는 그대로
    }

    @Test
    void reissue_fail() {
     assertThrows(RuntimeException.class, () -> authService.reissue("refreshToken"));
    }
}