package com.naranghiking.board.dao;

import com.naranghiking.board.dto.BoardDetailResponse;
import com.naranghiking.board.dto.BoardListResponse;
import com.naranghiking.board.dto.BoardRequest;
import com.naranghiking.common.dto.ImageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    // 게시글 수정 시 기존 이미지 삭제
    void deleteImages(List<String> deletedImages);
    // 게시글 삭제(논리적)
    int deleteById(Long id);
    // 물리적 삭제를 위한 id 리스트 조회
    List<Long> selectExpiredData(@Param("pivot") int pivot);
    // id 리스트 기반으로 삭제해야 하는 이미지들 조회
    List<String> selectExpiredImages(@Param("idList") List<Long> idList);
    // id 리스트 기반으로 게시글 삭제
    void deleteExpiredBoards(@Param("idList") List<Long> idList);
}
