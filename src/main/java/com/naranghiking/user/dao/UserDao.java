package com.naranghiking.user.dao;

import com.naranghiking.user.dto.SignUpRequest;
import com.naranghiking.user.dto.UpdateRequest;
import com.naranghiking.user.dto.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao {
    // 전체 유저 조회
    List<User> selectAll();
    
    // email로 유저 조회
    User select(Long userId);

    User findByEmail(String email);

    User findById(Long userId);

    int insert(SignUpRequest request);

    int update(UpdateRequest request);

    void delete(Long userId);
    // 논리적 삭제 유저 데이터 물리적 삭제 실시
    void deleteExpiredUsers(@Param("pivot") int pivot);
}
