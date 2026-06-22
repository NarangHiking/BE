package com.naranghiking.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {
    private Long id;
    private String email;
    private String pass;
    private String name;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    public UserResponse toResponse() {
        return new UserResponse(email, name, role, createdAt);
    }
}
