package com.naranghiking.board.dao;

import com.naranghiking.board.dto.BoardDetailResponse;
import com.naranghiking.board.dto.BoardListResponse;
import com.naranghiking.board.dto.BoardRequest;
import com.naranghiking.board.dto.BoardResponse;
import com.naranghiking.common.dto.ImageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper
public interface BoardDao {
    // 전체 게시글 조회
    List<BoardListResponse> selectAll(String keyword, String category);
    // 게시글 단건 조회
    BoardDetailResponse selectById(Long id);
    // 게시글 생성
    int insert(BoardRequest board);
    // 게시글 생성 시 이미지 저장
    void insertImages(@Param("id") Long id, @Param("images")List<ImageRequest> images);
    // 게시글 수정
    int update(@Param("id") Long id, @Param("board") BoardRequest board);
    // 게시글 삭제
    int deleteById(Long id);
}
