package com.naranghiking.user.dto;

import lombok.Getter;

@Getter
public class SignUpRequest {
    private String userId;
    private String email;
    private String password;
    private String name;
}
