package com.naranghiking.controller;

import com.naranghiking.model.dto.LoginRequest;
import com.naranghiking.model.dto.SignUpRequest;
import com.naranghiking.service.AuthService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/auth")
@RestController
@Slf4j
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    ResponseEntity<String> login(@RequestBody LoginRequest request, HttpSession session) {
        return service.login(request, session);
    }

    @PostMapping("/logout")
    ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null)
            session.invalidate();
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/register")
    ResponseEntity<String> register(@RequestBody SignUpRequest request) {
        try {
            service.register(request);
            return ResponseEntity.ok("signup ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("bad request");
        }
    }
}
