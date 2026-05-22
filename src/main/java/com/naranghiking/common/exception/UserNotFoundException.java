package com.naranghiking.common.exception;

public class UserNotFoundException extends RuntimeException {
 public UserNotFoundException(String message) {
     super(message);
 }
}