package com.naranghiking.common.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

// 전체 앱을 실행하기 떄문에 SpringBoot는 Redis와 DB 전부 필요함.
//@SpringBootTest =>
@ExtendWith(MockitoExtension.class)
class JwtUtilTest {
	
	private JwtUtil jwtUtil;
	
	@BeforeEach
	void setUp() {
		jwtUtil = new JwtUtil();
		ReflectionTestUtils.setField(jwtUtil, "secretKey", "naranghiking-secret-key-must-be-at-least-32-bytes");
        ReflectionTestUtils.setField(jwtUtil, "accessExpiration", 1800000L);
        ReflectionTestUtils.setField(jwtUtil, "refreshExpiration", 604800000L);
	}
	
	@Test
	void generateAccessToken() {
		String token = jwtUtil.generateAccessToken("123", "USER");
		assertNotNull(token);
		assertEquals("123", jwtUtil.getUserId(token));
	}
	
	@Test
	void isNotExpired() {
		String token = jwtUtil.generateAccessToken("123", "USER");
		assertFalse(jwtUtil.isExpired(token));
	}
	
	@Test
	void getReaminExpiration() {
		String token = jwtUtil.generateAccessToken("123", "USER");
		assertTrue(jwtUtil.getRemainExpiration(token) > 0);
	}
}
