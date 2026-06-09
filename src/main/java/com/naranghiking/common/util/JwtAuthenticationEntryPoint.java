package com.naranghiking.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naranghiking.common.dto.ApiResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 응답 헤더는 JSON 형식
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401 에러

        // redis에 저장된 토큰과 요청에 사용된 토큰이 다를 때
        ApiResult<String> failResult = ApiResult.fail("인증에 실패했습니다. 유효한 토큰이 필요합니다.");

        // 객체를 JSON 문자열로 바꿔서 응답 바디에 직접 쓰기
        String responseBody = objectMapper.writeValueAsString(failResult);
        response.getWriter().write(responseBody);
    }
}
