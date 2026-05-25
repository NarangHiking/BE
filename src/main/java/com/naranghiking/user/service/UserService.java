package com.naranghiking.user.service;

import com.naranghiking.user.dto.SignUpRequest;
import com.naranghiking.user.dto.User;

import java.util.List;

public interface UserService {

    public void insert(SignUpRequest request);

    public User select(String email);

    public List<User> selectAll();

    public boolean checkPassword(String rawPassword, String encodedPassword);
}
