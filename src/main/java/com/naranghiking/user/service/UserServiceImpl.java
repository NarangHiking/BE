package com.naranghiking.user.service;

import com.naranghiking.auth.service.TokenService;
import com.naranghiking.common.exception.UserNotFoundException;
import com.naranghiking.user.dao.UserDao;
import com.naranghiking.user.dto.SignUpRequest;
import com.naranghiking.user.dto.UpdateRequest;
import com.naranghiking.user.dto.User;
import com.naranghiking.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;
    private final TokenService tokenService;

    public void insert(SignUpRequest request) {
        User user = userDao.findByEmail(request.getEmail());
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
    public User findById(Long userId) {
        return userDao.findById(userId);
    }

    // 사용자 전달용, 관리자만
    @Override
    public UserResponse selectById(Long userId) {
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

    public int update(Long userId, UpdateRequest request) {
        // 비밀번호 검증을 위해 저장된 해시가 포함된 사용자 조회 (select는 pass를 안 가져옴)
        User user = userDao.findById(userId);
        if (user == null) {
            throw new UserNotFoundException("해당 사용자가 존재하지 않습니다.");
        }
        // 현재 비밀번호 재확인: 본인이 입력한 현재 비밀번호가 저장된 값과 일치해야 수정 허용
        if (request.getCurrentPass() == null
                || !passwordEncoder.matches(request.getCurrentPass(), user.getPass())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }
        // 새 비밀번호가 있을 때만 인코딩해서 변경 (없으면 pass 컬럼은 건드리지 않음 → 이름만 수정 가능)
        if (request.getPass() != null && !request.getPass().isBlank()) {
            request.setPass(passwordEncoder.encode(request.getPass()));
        }
        request.setUserId(userId);
        return userDao.update(request);
    }

    public int resetPassword(UpdateRequest request) {
        User user = userDao.findByEmailAndName(request.getEmail(), request.getName());
        if (user == null) {
            throw new UserNotFoundException("해당 사용자가 존재하지 않습니다.");
        }
        request.setUserId(user.getId());
        request.setName(user.getName());
        request.setPass(passwordEncoder.encode(request.getPass()));
        return userDao.update(request);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public void delete(Long userId, String accessToken) {
        User user = userDao.findById(userId); // select 는 deleted_at 을 가져오지 않으므로 select * 인 findById 사용
        if (user == null || user.getDeletedAt() != null) {
            throw new UserNotFoundException("이미 탈퇴한 사용자입니다.");
        }
        tokenService.blacklistAccessToken(accessToken);
        tokenService.deleteRefreshToken(userId);
        userDao.delete(userId);
    }
}
