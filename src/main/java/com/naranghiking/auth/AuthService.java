package com.naranghiking.auth;

import com.naranghiking.auth.dto.LoginRequest;
import com.naranghiking.user.UserService;
import com.naranghiking.user.dto.SignUpRequest;
import com.naranghiking.user.dto.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        User user = userService.findByEmail(request.getEmail());
        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("login fail");
        if (!userService.checkPassword(request.getPassword(), user.getPassword()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login fail");
        return ResponseEntity.ok("login ok");
    }
}
