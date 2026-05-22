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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.naranghiking.auth.dto.LoginRequest;
import com.naranghiking.common.util.JwtUtil;
import com.naranghiking.user.dto.User;
import com.naranghiking.user.service.UserService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
	
	@Mock
	private UserService userService;
	
	@Mock
	private TokenService tokenService;
	
	@Mock
	private JwtUtil jwtUtil;
	
	// 가짜 객체 주입
	@InjectMocks
	private AuthService authService;

	// 가짜 객체로 테스트 진행 => 실제로 비밀번호가 일치하는지 확인이 불가능
	// 추후 통합 테스트 필요
	@Test
	void login_success() {
		//given
		LoginRequest request = new LoginRequest("test","test@test.com", "password");
		User user = new User(1L, "test", "test@test.com", "password");
		
		when(userService.findByEmail("test@test.com")).thenReturn(user);
		when(userService.checkPassword("password", user.getPassword())).thenReturn(true);
		when(jwtUtil.generateAccessToken("1")).thenReturn("accessToken");
		when(jwtUtil.generateRefreshToken("1")).thenReturn("refreshToken");
		
		// when
		ResponseEntity<?> response = authService.login(request);
		
		//then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(tokenService).saveRefreshToken("1", "refreshToken");
	}
	
	@Test
	void login_userNotFound() {
		//given
		LoginRequest request = new LoginRequest("test", "test@test.com", "password");
		when(userService.findByEmail("test@test.com")).thenReturn(null);
		
		//when
		ResponseEntity<?> response = authService.login(request);
		
		//then
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	void login_wrongPassword() {
		//given
		LoginRequest request = new LoginRequest("test", "test@test.com", "password");
		User user = new User(1L, "test", "test@test.com", "password");
		when(userService.findByEmail("test@test.com")).thenReturn(user);
		when(userService.checkPassword(any(), any())).thenReturn(false);
		
		// when
		ResponseEntity<?> response = authService.login(request);
		
		// then
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}
	
	@Test
	void logout() {
		authService.logout("1", "accessToken");
		
		verify(tokenService).blacklistAccessToken("accessToken");
		verify(tokenService).deleteRefreshToken("1");
	}
	
	@Test
	void reissue_success() {
		when(tokenService.getRefreshToken("1")).thenReturn("refreshToken");
		when(jwtUtil.generateAccessToken("1")).thenReturn("newAccessToken");
		
		String result = authService.reissue("1", "refreshToken");
		
		assertEquals("newAccessToken", result);
	}
	
    @Test
    void reissue_fail() {
        when(tokenService.getRefreshToken("1")).thenReturn("differentToken");

        assertThrows(RuntimeException.class,
                () -> authService.reissue("1", "refreshToken"));
    }
}
