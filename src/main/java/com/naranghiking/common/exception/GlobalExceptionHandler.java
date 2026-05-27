
package com.naranghiking.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.naranghiking.common.dto.ApiResult;

import java.util.NoSuchElementException;

@Slf4j
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

	@ExceptionHandler(MethodArgumentNotValidException.class) // 컨트롤러에서 @Valid에 의해 막혔을 경우 넘어옴
	public ResponseEntity<ApiResult<Void>> handleValidationExceptions(MethodArgumentNotValidException e) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ApiResult.fail("필수 정보가 누락되었습니다."));
	}

	@ExceptionHandler(NoSuchElementException.class) // 해당 컨텐트가 없을 경우 넘어옴
	public ResponseEntity<ApiResult<Void>> handleNotFoundExceptions(NoSuchElementException e) {
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(ApiResult.fail(e.getMessage()));
	}

	@ExceptionHandler(RuntimeException.class) // 그 외의 Transactional을 위해 예외를 던질 경우
	public ResponseEntity<ApiResult<Void>> handleRuntimeExceptions(RuntimeException e) {
		log.error("RuntimeException 발생: " + e);
		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ApiResult.fail(e.getMessage()));
	}
}
