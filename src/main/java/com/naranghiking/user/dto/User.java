package com.naranghiking.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class User {
    private Integer id;
    private String email;
    private String pass;
    private String name;
    private String role;
    private LocalDateTime createAt;
    private LocalDateTime removeAt;

    public User(String email, String pass, String name, String role) {
        this.email = email;
        this.pass = pass;
        this.name = name;
        this.role = role;
    }
}
