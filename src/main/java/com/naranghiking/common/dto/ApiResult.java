package com.naranghiking.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "API 공통 응답 포맷")
public class ApiResult<T> {
	
	@Schema(description = "성공 여부", example="true")
	private boolean success;
	
	@Schema(description = "실패 시 에러 메시지 (성공 시 null)", example = "null")
	private String error;
	
	@Schema(description = "성공 시 반환할 객체 (실패 시 null)")
	private T data;

	public ApiResult(boolean success, String error, T data) {
		this.success = success;
		this.error = error;
		this.data = data;
	}
	
	public static <T> ApiResult<T> success(T data) {
		return new ApiResult<>(true, null, data);
	}
	
	public static <T> ApiResult<T> fail(String message) {
		return new ApiResult<>(false, message, null);
	}
}
