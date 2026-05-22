package com.naranghiking.user.service;

import com.naranghiking.user.dto.SignUpRequest;
import com.naranghiking.user.dto.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

    HashMap<String, User> users = new HashMap<>();

    @PostConstruct
    void init() {
        //테스트 데이터 생성
        users.put("test@test.com", new User("1","test@test.com", passwordEncoder.encode("1234"), "woochan"));
    }

    public void register(SignUpRequest request) {
        if (users.containsKey(request.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        users.put(request.getEmail(), new User(request.getUserId(),request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getName()));
    }

    public User findByEmail(String email) {
        return users.get(email);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
