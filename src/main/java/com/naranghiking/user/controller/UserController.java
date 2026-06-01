package com.naranghiking.user.controller;

import com.naranghiking.auth.service.AuthService;
import com.naranghiking.user.dto.UpdateRequest;
import com.naranghiking.user.dto.User;
import com.naranghiking.user.dto.UserResponse;
import com.naranghiking.user.service.UserServiceImpl;
import com.naranghiking.common.dto.ApiResult;
import com.naranghiking.user.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/user")
@ResponseBody
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserServiceImpl userService;
    private final AuthService authService;

    @GetMapping("/list")
    ResponseEntity<ApiResult> selectAll() {
        List<UserResponse> users = userService.selectAll();
        return ResponseEntity.ok(ApiResult.success(users));
    }

    @GetMapping
    ResponseEntity<ApiResult> select(
            @AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(ApiResult.success(userService.selectById(userId)));
    }

    @PostMapping
    ResponseEntity<ApiResult> register(@RequestBody SignUpRequest request) {
        userService.insert(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResult.success("signup ok"));
    }

    // @PutMapping 속성 전체 교체
    // @PatchMapping 속성 중 일부만 교체
    // JWT 토큰을 사용하기 때문에 PathVariable로 식별자를 안꺼내와도 됨
    // => JWT 토큰에서 식별자(이메일) 추출해서 사용
    @PatchMapping
    ResponseEntity<ApiResult> update(
            @AuthenticationPrincipal String userId,
            @RequestBody UpdateRequest request){
        userService.update(userId, request);
        return ResponseEntity.ok(ApiResult.success(null));
    }

    @PatchMapping("/remove")
    ResponseEntity<ApiResult> remove(
            @AuthenticationPrincipal String userId,
            @RequestHeader("Authorization") String bearer
    ){
        userService.delete(userId, bearer);
        return ResponseEntity.ok(ApiResult.success(null));
    }
}
