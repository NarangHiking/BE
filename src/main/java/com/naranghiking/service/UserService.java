package com.naranghiking.service;

import com.naranghiking.model.dto.LoginRequest;
import com.naranghiking.model.dto.User;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;

@Service
public class UserService {

    HashMap<String, User> users = new HashMap<>();

    @PostConstruct
    void init() {
        //테스트 데이터 생성
        users.put("test@test.com", new User("test@test.com", "1234", "woochan"));
    }

    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpSession session) {
        User user = users.get(request.getEmail());
        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("login fail");
        String password = user.getPassword();
        if (password == null || !password.equals(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login fail");
        }
        session.setAttribute("loginUser", user);
        return ResponseEntity.ok("login ok");
    }
}
