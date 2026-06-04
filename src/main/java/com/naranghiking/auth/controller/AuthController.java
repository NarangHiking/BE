package com.naranghiking.auth.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naranghiking.auth.dto.LoginRequest;
import com.naranghiking.auth.dto.TokenResponse;
import com.naranghiking.auth.service.AuthService;
import com.naranghiking.common.dto.ApiResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/api/auth")
@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name="인증 API", description="로그인, 로그아웃 API")
public class AuthController {

    private final AuthService service;
    
    @Operation(summary = "로그인", description = "사용자 정보를 확인합니다")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="로그인 성공"),
        @ApiResponse(responseCode="401", description="사용자를 찾을 수 없습니다."),
        @ApiResponse(responseCode="401", description="비밀번호가 일치하지 않습니다."),
    })
    @PostMapping("/login")
    ResponseEntity<ApiResult<TokenResponse>> login(@RequestBody LoginRequest request) {
    	TokenResponse data = service.login(request);
        return ResponseEntity.ok(ApiResult.success(data));
    }

    @Operation(summary = "로그아웃", description = "")
    @ApiResponses({
    	@ApiResponse(responseCode="200", description="로그아웃 성공"),
    })
    @PostMapping("/logout")
    ResponseEntity<ApiResult<Void>> logout(
    		@Parameter(hidden = true)
    		@AuthenticationPrincipal Long userId ,
    		@RequestHeader("Authorization") String bearer) {
    	String token = bearer.substring(7);
    	service.logout(userId, token);
    	return ResponseEntity.ok(ApiResult.success(null));
    }
      
    @Operation(summary = "토큰 재발급", description = "access token을 재발급하기 위해 refresh token을 발급합니다.")
    @ApiResponses({
    	@ApiResponse(responseCode="200", description="토큰 재발급 성공"),
    	@ApiResponse(responseCode="401", description="인증 실패 (토큰 만료 또는 토큰 불일치)"),
    })
    
    @PostMapping("/reissue")
    ResponseEntity<ApiResult<TokenResponse>> reissue(@RequestBody Map<String, String> body) {
    	
    	String refreshToken = body.get("refreshToken");
    	TokenResponse data = service.reissue(refreshToken);
    	
    	return ResponseEntity.ok(ApiResult.success(data));
    }
}
