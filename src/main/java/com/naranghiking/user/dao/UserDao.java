package com.naranghiking.user.dao;

import com.naranghiking.user.dto.SignUpRequest;
import com.naranghiking.user.dto.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {
    // 전체 유저 조회
    List<User> selectAll();
    
    // email로 유저 조회
    User select(String email);

    int insert(SignUpRequest sign);

    int update(User user);

    void delete(int id);
}
