package com.naranghiking.board.service;


import com.naranghiking.board.dto.BoardRequest;
import com.naranghiking.board.dto.BoardResponse;

import java.util.List;

public interface BoardService {
    // 전체 게시글 조회
    List<BoardResponse> selectAll(String keyword, String category);
    // 게시글 단건 조회
    BoardResponse selectById(Long id);
    // 게시글 생성
    int insert(BoardRequest board);
}
