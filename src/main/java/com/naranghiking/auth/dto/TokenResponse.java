package com.naranghiking.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description="로그인 성공 응답 객체")
public class TokenResponse {
	
	@Schema(description = "액세스 토큰 (JWT)", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
	private String accessToken;
	
	@Schema(description="리프레시 토큰", example="\"6f8902c3-4d5e-6f7a-8b9c...\"")
	private String refreshToken;
	
	@Schema(description="사용자 아이디", example="0L")
	private Long userId;
}
