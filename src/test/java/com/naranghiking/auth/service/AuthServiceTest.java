package com.naranghiking.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

import com.naranghiking.auth.dto.TokenResponse;
import com.naranghiking.common.exception.UserNotFoundException;
import com.naranghiking.common.util.JwtUtil;
import com.naranghiking.user.dao.UserDao;
import com.naranghiking.user.dto.User;
import com.naranghiking.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.naranghiking.auth.dto.LoginRequest;
import com.naranghiking.user.service.UserServiceImpl;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserService userServiceImpl;  // UserDao 아닌 UserService
    @Mock
    TokenService tokenService;
    @Mock
    JwtUtil jwtUtil;

    @InjectMocks
    AuthService authService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(2L);
        mockUser.setEmail("hong@test.com");
        mockUser.setPass("encodedPassword");
        mockUser.setRole("USER");
    }

    @Test
    void login_userNotFound() {
        given(userServiceImpl.findByEmail("wrongEmail")).willReturn(null);

        assertThrows(UserNotFoundException.class,
                () -> authService.login(new LoginRequest("wrongEmail", "pass1234")));
    }

    @Test
    void login_wrongPassword() {
        given(userServiceImpl.findByEmail("hong@test.com")).willReturn(mockUser);
        given(userServiceImpl.checkPassword("wrongpassword", mockUser.getPass())).willReturn(false);

        assertThrows(BadCredentialsException.class,
                () -> authService.login(new LoginRequest("hong@test.com", "wrongpassword")));
    }

    @Test
    void login_success() {
        given(userServiceImpl.findByEmail("hong@test.com")).willReturn(mockUser);
        given(userServiceImpl.checkPassword("pass1234", mockUser.getPass())).willReturn(true);
        given(jwtUtil.generateAccessToken(2L, "USER")).willReturn("accessToken");
        given(jwtUtil.generateRefreshToken(2L, "USER")).willReturn("refreshToken");

        TokenResponse response = authService.login(new LoginRequest("hong@test.com", "pass1234"));

        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        verify(tokenService).saveRefreshToken(2L, "refreshToken");
    }

    @Test
    void logout_callsTokenService() {
        authService.logout(2L, "accessToken");
        verify(tokenService).blacklistAccessToken("accessToken");
        verify(tokenService).deleteRefreshToken(2L);
    }
}