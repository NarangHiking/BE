package com.naranghiking.service;

import com.naranghiking.model.dto.LoginRequest;
import com.naranghiking.model.dto.SignUpRequest;
import com.naranghiking.model.dto.User;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;

@Service
public class AuthService {

    HashMap<String, User> users = new HashMap<>();

    private final PasswordEncoder passwordEncoder;

    public AuthService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    void init() {
        //테스트 데이터 생성
        users.put("test@test.com", new User("test@test.com", passwordEncoder.encode("1234"), "woochan"));
    }

    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        User user = users.get(request.getEmail());
        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("login fail");
        String encodedPassword = user.getPassword();

        if (encodedPassword == null || !passwordEncoder.matches(request.getPassword(), encodedPassword))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login fail");
        return ResponseEntity.ok("login ok");
    }

    public void register(SignUpRequest request) {
        if (users.containsKey(request.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        users.put(request.getEmail(), new User(request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getName()));
    }
}
