package com.naranghiking.user.controller;

import com.naranghiking.user.service.UserService;
import com.naranghiking.user.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/user")
@ResponseBody
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    ResponseEntity<String> register(@RequestBody SignUpRequest request) {
        try {
            userService.register(request);
            return ResponseEntity.ok("signup ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("bad request");
        }
    }
}
