package com.naranghiking.board.service;


import com.naranghiking.board.dto.BoardResponse;

import java.util.List;

public interface BoardService {
    // 전체 게시글 조회
    List<BoardResponse> selectAll();
}
