package com.naranghiking.user.service;

import com.naranghiking.user.dao.UserDao;
import com.naranghiking.user.dto.SignUpRequest;
import com.naranghiking.user.dto.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;

    public void insert(SignUpRequest request) {

        User user = userDao.select(request.getEmail());
        if (user == null) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        userDao.insert(request);
    }

    public User select(String email) {
        return userDao.select(email);
    }

    public List<User> selectAll() {
        return userDao.selectAll();
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
