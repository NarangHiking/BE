package com.naranghiking.common.exception;

public class TokenExpiredException extends RuntimeException{
	public TokenExpiredException(String message) {
		super(message);
	}
}
