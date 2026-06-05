package com.naranghiking.board.service;


import com.naranghiking.board.dto.BoardDetailResponse;
import com.naranghiking.board.dto.BoardListResponse;
import com.naranghiking.board.dto.BoardRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {
    // 전체 게시글 조회
    List<BoardListResponse> selectAll(String keyword, String category);
    // 게시글 단건 조회
    BoardDetailResponse selectById(Long id);
    // 게시글 생성
    void insert(BoardRequest board, List<MultipartFile> images);
    // 게시글 수정
    BoardDetailResponse update(Long id, BoardRequest board, List<MultipartFile> addedImages, List<String> deletedImages);
    // 게시글 삭제
    void deleteById(Long id, Long userId);
}
