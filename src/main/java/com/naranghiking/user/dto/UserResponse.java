package com.naranghiking.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class UserResponse {
    private String email;
    private String name;
    private String role;
    private LocalDateTime createdAt;
}
