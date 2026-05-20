package com.naranghiking.auth.dto;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String userId;
    private String email;
    private String password;
}
