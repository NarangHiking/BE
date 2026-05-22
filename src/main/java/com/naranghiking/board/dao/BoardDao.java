package com.naranghiking.board.dao;

import com.naranghiking.board.dto.BoardResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardDao {
    // 전체 게시글 조회
    List<BoardResponse> selectAll();
}
