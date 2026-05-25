package com.naranghiking.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

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
		User user = new User("hong@test.com", "pass1234", "홍길동", "ADMIN");
        LoginRequest request = new LoginRequest("2", "hong@test.com", "pass1234");

		// when
		TokenResponse response = authService.login(request);
		
		//then
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
        assertNotNull(response.getUserId());

        // (선택) 토큰에서 추출한 정보가 맞는지
        String userId = jwtUtil.getUserId(response.getAccessToken());
        assertEquals("2", userId);
	}
	
	@Test
	void login_userNotFound() {
		//given
		LoginRequest request = new LoginRequest("test", "test@test.com", "password");

		//when
		assertThrows(IllegalArgumentException.class, () -> authService.login(request));
	}
	
	@Test
	void login_wrongPassword() {
		//given
		LoginRequest request = new LoginRequest("test", "test@test.com", "password");
		User user = new User("1", "test", "test@test.com", "password");

		// when
		assertThrows(BadCredentialsException.class, () -> authService.login(request));
	}
	
	@Test
	void logout() {
		authService.logout("1", "accessToken");
		
		verify(tokenService).blacklistAccessToken("accessToken");
		verify(tokenService).deleteRefreshToken("1");
	}
	
	@Test
	void reissue_success() {
		TokenResponse result = authService.reissue("refreshToken");
		assertEquals("newAccessToken", result.getAccessToken());
	}
	
    @Test
    void reissue_fail() {
        assertThrows(RuntimeException.class,
                () -> authService.reissue("refreshToken"));
    }
}
