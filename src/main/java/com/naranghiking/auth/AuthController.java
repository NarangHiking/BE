package com.naranghiking.auth;

import com.naranghiking.auth.dto.LoginRequest;
import com.naranghiking.user.dto.SignUpRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
@Slf4j
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    ResponseEntity<String> login(@RequestBody LoginRequest request) {
        return service.login(request);
    }

    @PostMapping("/logout")
    ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null)
            session.invalidate();
        return ResponseEntity.ok("ok");
    }
}
