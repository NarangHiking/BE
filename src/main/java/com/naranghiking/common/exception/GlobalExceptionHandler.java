
package com.naranghiking.common.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.naranghiking.common.dto.ApiResult;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<ApiResult<Void>> handleTokenExpired(TokenExpiredException e) {
		return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(ApiResult.fail(e.getMessage()));
	}
	
	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<ApiResult<Void>> handleInvalidToken(InvalidTokenException e) {
		return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(ApiResult.fail(e.getMessage()));
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiResult<Void>> handleUserNotFound(UserNotFoundException e) {
	    return ResponseEntity
	            .status(HttpStatus.NOT_FOUND)
	            .body(ApiResult.fail(e.getMessage()));
	}

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResult<Void>> handleBadCredentials(BadCredentialsException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResult.fail("비밀번호가 일치하지 않습니다."));
    }
}
