package com.naranghiking.auth.controller;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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

    // 응답 헤더에 쿠키로 Token 삽입
    private void setTokenCookies(HttpServletResponse resp, String accessToken, String refreshToken) {
        ResponseCookie access = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(false) // 운영 배포 시 true로 변경
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .sameSite("Strict")
                .build();

        ResponseCookie refresh = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/api/auth")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();

        resp.addHeader(HttpHeaders.SET_COOKIE, access.toString());
        resp.addHeader(HttpHeaders.SET_COOKIE, refresh.toString());
    }

    @Operation(summary = "로그인", description = "사용자 정보를 확인합니다")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="로그인 성공"),
        @ApiResponse(responseCode="401", description="사용자를 찾을 수 없습니다."),
        @ApiResponse(responseCode="401", description="비밀번호가 일치하지 않습니다."),
    })
    @PostMapping("/login")
    ResponseEntity<ApiResult> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
    	TokenResponse data = service.login(request);
        setTokenCookies(response, data.getAccessToken(), data.getRefreshToken());
        return ResponseEntity.ok(ApiResult.success(Map.of("userId", data.getUserId())));
    }

    private void clearTokenCookies(HttpServletResponse response) {
        ResponseCookie access = ResponseCookie.from("accessToken", "")
                .httpOnly(true).path("/").maxAge(0).build();
        ResponseCookie refresh = ResponseCookie.from("refreshToken", "")
                .httpOnly(true).path("/api/auth").maxAge(0).build();
        response.addHeader(HttpHeaders.SET_COOKIE, access.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refresh.toString());
    }

    @Operation(summary = "로그아웃", description = "")
    @ApiResponses({
    	@ApiResponse(responseCode="200", description="로그아웃 성공"),
    })
    @PostMapping("/logout")
    ResponseEntity<ApiResult> logout(
            @AuthenticationPrincipal Long userId,
            HttpServletRequest req,
            HttpServletResponse resp) {
        if (req.getCookies() != null) {
            // 쿠키에서 토큰을 읽어 블랙리스트에 등록
            Arrays.stream(req.getCookies())
                    .filter(c -> "accessToken".equals(c.getName()))
                    .findFirst()
                    .ifPresent(c -> service.logout(userId, c.getValue()));
        }
        clearTokenCookies(resp);
    	return ResponseEntity.ok(ApiResult.success("ok"));
    }

    @Operation(summary = "토큰 재발급", description = "access token을 재발급하기 위해 refresh token을 발급합니다.")
    @ApiResponses({
    	@ApiResponse(responseCode="200", description="토큰 재발급 성공"),
    	@ApiResponse(responseCode="401", description="인증 실패 (토큰 만료 또는 토큰 불일치)"),
    })

    @PostMapping("/reissue")
    ResponseEntity<ApiResult> reissue(
            HttpServletRequest req,
            HttpServletResponse resp
    ) {
        if (req.getCookies() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResult.fail("refreshToken이 없습니다"));
        }
        String refreshToken = Arrays.stream(req.getCookies())
                .filter(c -> "refreshToken".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResult.fail("refreshToken이 없습니다"));
        }

        TokenResponse data = service.reissue(refreshToken);
        ResponseCookie access = ResponseCookie.from("accessToken", data.getAccessToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .sameSite("Strict")
                .build();
        resp.addHeader(HttpHeaders.SET_COOKIE, access.toString());
    	return ResponseEntity.ok(ApiResult.success("ok"));
    }

}
