package com.naranghiking.user.controller;

import com.naranghiking.user.service.UserServiceImpl;
import com.naranghiking.common.dto.ApiResult;
import com.naranghiking.user.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@ResponseBody
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/register")
    ResponseEntity<ApiResult> register(@RequestBody SignUpRequest request) {
        try {
            userService.insert(request);
            return ResponseEntity.ok(ApiResult.success("signup ok"));
        } catch (Exception e) {
        	return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResult.fail("bad request"));
        }
    }
}
