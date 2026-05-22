package com.naranghiking.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class LoginRequest {
    private String userId;
    private String email;
    private String password;
}
