package com.naranghiking.user.controller;

import com.naranghiking.auth.service.AuthService;
import com.naranghiking.user.dto.UpdateRequest;
import com.naranghiking.user.dto.User;
import com.naranghiking.user.dto.UserResponse;
import com.naranghiking.user.service.UserServiceImpl;
import com.naranghiking.common.dto.ApiResult;
import com.naranghiking.user.dto.SignUpRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RequestMapping("/api/user")
@ResponseBody
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping("/list")
    ResponseEntity<ApiResult> selectAll() {
        List<UserResponse> users = userService.selectAll();
        return ResponseEntity.ok(ApiResult.success(users));
    }

    @GetMapping
    ResponseEntity<ApiResult> select(
            @AuthenticationPrincipal Long userId) {
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
            @AuthenticationPrincipal Long userId,
            @RequestBody UpdateRequest request){
        userService.update(userId, request);
        return ResponseEntity.ok(ApiResult.success("ok"));
    }

    @PatchMapping("/remove")
    ResponseEntity<ApiResult> remove(
            @AuthenticationPrincipal Long userId,
            HttpServletRequest req
    ){
        String token = null;
        if (req.getCookies() != null) {
            token = Arrays.stream(req.getCookies())
                    .filter(c -> "accessToken".equals(c.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }
        userService.delete(userId, token);
        return ResponseEntity.ok(ApiResult.success("ok"));
    }
}
