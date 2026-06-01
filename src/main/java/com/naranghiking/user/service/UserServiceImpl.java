package com.naranghiking.user.service;

import com.naranghiking.common.exception.UserNotFoundException;
import com.naranghiking.user.dao.UserDao;
import com.naranghiking.user.dto.SignUpRequest;
import com.naranghiking.user.dto.UpdateRequest;
import com.naranghiking.user.dto.User;
import com.naranghiking.user.dto.UserResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;

    public void insert(SignUpRequest request) {
        User user = userDao.select(request.getEmail());
        if (user != null) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        String encoded = passwordEncoder.encode(request.getPass());
        request.setPass(encoded);
        userDao.insert(request);
    }

    // 내부 전달용
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    // 내부 전달용
    @Override
    public User findById(String userId) {
        return userDao.findById(userId);
    }

    // 사용자 전달용
    @Override
    public UserResponse selectById(String userId) {
        return null;
    }

    public UserResponse select(String userId) {
        return userDao.select(userId).toResponse();
    }

    public List<UserResponse> selectAll() {
        List<User> users = userDao.selectAll();
        List<UserResponse> userResponses = new ArrayList<>();

        for (User u : users) {
            userResponses.add(u.toResponse());
        }
        return userResponses;
    }

    public int update(String userId, UpdateRequest request) {
        User user = userDao.select(userId);
        if (user == null) {
            throw new UserNotFoundException("해당 사용자가 존재하지 않습니다.");
        }
        String encoded = passwordEncoder.encode(request.getPass());
        request.setPass(encoded);
        request.setUserId(userId);
        return userDao.update( request);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
