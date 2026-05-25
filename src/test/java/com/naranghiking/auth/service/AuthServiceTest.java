package com.naranghiking.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import com.naranghiking.common.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.data.redis.autoconfigure.DataRedisAutoConfiguration;
import org.springframework.boot.data.redis.autoconfigure.DataRedisRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;

import com.naranghiking.auth.dto.LoginRequest;
import com.naranghiking.auth.dto.TokenResponse;
import com.naranghiking.common.util.JwtUtil;
import com.naranghiking.user.dto.User;
import com.naranghiking.user.service.UserServiceImpl;

@SpringBootTest
class AuthServiceTest {
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthService authService;


//        ('admin@naranghiking.com', 'admin1234', '관리자', 'ADMIN'),
//                ('hong@test.com', 'pass1234', '홍길동', 'USER'),

    @Test
	void login_success() {
		//given
        LoginRequest request = new LoginRequest("2", "hong@test.com", "pass1234");

		// when
		TokenResponse response = authService.login(request);
		
		//then
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
        assertNotNull(response.getUserId());

        String userId = jwtUtil.getUserId(response.getAccessToken());
        assertEquals("2", userId);
	}
	
	@Test
	void login_userNotFound() {
		//given
        LoginRequest request = new LoginRequest("-1", "wrongEmail", "pass1234");

		//when
		assertThrows(UserNotFoundException.class, () -> authService.login(request));
	}
	
	@Test
	void login_wrongPassword() {
		//given
        LoginRequest request = new LoginRequest("2", "hong@test.com", "wrongpassword");

		// when
		assertThrows(BadCredentialsException.class, () -> authService.login(request));
	}
	
//	@Test
//	void logout() {
//        //given
//        LoginRequest request = new LoginRequest("2", "hong@test.com", "pass1234");
//        TokenResponse loginResponse = authService.login(request);
//        String userId = loginResponse.getUserId();
//        String accessToken = loginResponse.getAccessToken();
//        assertNotNull(tokenService.getRefreshToken(userId));
//
//        //when
//        authService.logout(userId, accessToken);
//
//        //then
//        assertNull(tokenService.getRefreshToken(userId));
//        assertTrue(tokenService.isBlacklisted(accessToken));
//    }
//
//	@Test
//	void reissue_success() {
//        // given — 먼저 로그인해서 진짜 refresh token 얻기
//        LoginRequest request = new LoginRequest("2","hong@test.com", "pass1234");
//        TokenResponse loginResponse = authService.login(request);
//        String refreshToken = loginResponse.getRefreshToken();
//
//        // when
//        TokenResponse result = authService.reissue(refreshToken);
//
//		TokenResponse result = authService.reissue("refreshToken");
//		assertEquals("newAccessToken", result.getAccessToken());
//	}
//
//    @Test
//    void reissue_fail() {
//        assertThrows(RuntimeException.class,
//                () -> authService.reissue("refreshToken"));
//    }
}
