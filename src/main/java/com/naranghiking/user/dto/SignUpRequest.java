package com.naranghiking.user.dto;

import lombok.Getter;

@Getter
public class SignUpRequest {
    private Long userId;
    private String email;
    private String password;
    private String name;
}
