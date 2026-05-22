package com.naranghiking.board.dao;

import com.naranghiking.board.dto.BoardRequest;
import com.naranghiking.board.dto.BoardResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardDao {
    // 전체 게시글 조회
    List<BoardResponse> selectAll();
    // 게시글 단건 조회
    BoardResponse selectById(long id);
    // 게시글 생성
    int insert(BoardRequest board);
}
