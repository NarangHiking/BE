package com.naranghiking.auth.controller;

import com.naranghiking.auth.service.AuthService;
import com.naranghiking.auth.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return service.login(request);
    }

    @PostMapping("/logout")
    ResponseEntity<?> logout(@AuthenticationPrincipal String userId ,
    		HttpServletRequest request) {
    	String token = resolveToken(request);
    	service.logout(userId, token);
    	return ResponseEntity.ok().build();
    }
    
    @PostMapping("/reissue")
    ResponseEntity<?> reissue(@AuthenticationPrincipal String userId, @RequestBody Map<String, String> body) {
    	String newAccessToken = service.reissue(userId, body.get("refreshToken"));
    	return ResponseEntity.status(HttpStatus.OK).body(Map.of("accessfreshToken", newAccessToken));
    }
    
    private String resolveToken(HttpServletRequest request) {
    	String bearer = request.getHeader("Authorization");
    	if (bearer != null && bearer.startsWith("Bearer ")) {
    		return bearer.substring(7);
    	}
    	return null;
    }
    
}
