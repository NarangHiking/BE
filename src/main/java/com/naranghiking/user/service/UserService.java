package com.naranghiking.user.service;

import com.naranghiking.user.dto.SignUpRequest;
import com.naranghiking.user.dto.User;
import com.naranghiking.user.dto.UserResponse;

import java.util.List;

public interface UserService {

    void insert(SignUpRequest request);
    User findByEmail(String email);      // 내부용 (pass 포함)
    User findById(Long userId);               // 내부용 (pass 포함)
    UserResponse selectById(Long userId);
    public List<UserResponse> selectAll();
    public boolean checkPassword(String rawPassword, String encodedPassword);

    void delete(Long userId, String accessToken);
}
