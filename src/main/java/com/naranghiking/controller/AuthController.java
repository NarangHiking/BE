package com.naranghiking.controller;

import com.naranghiking.model.dto.LoginRequest;
import com.naranghiking.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@RequestMapping("api/auth")
@Controller
@Slf4j
public class AuthController {

    private final UserService service;

    public AuthController(UserService service) {
        this.service = service;
    }

    @PostMapping("/login")
    ResponseEntity<String> login(@RequestBody LoginRequest request, HttpSession session) {
        return service.login(request, session);
    }
}
