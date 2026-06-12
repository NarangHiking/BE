package com.naranghiking.board.dao;

import com.naranghiking.board.dto.BoardCommentRequest;
import com.naranghiking.board.dto.BoardCommentResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BoardCommentDao {
    // 댓글 조회
    BoardCommentResponse selectById(Long id);
    // 댓글 작성
    int insert(@Param("boardId") Long boardId, @Param("comment") BoardCommentRequest comment);
    // 댓글 수정
    int update(@Param("comment") BoardCommentRequest comment);
    // 댓글 삭제(논리적)
    int delete(Long commentId);
    // 댓글 삭제(물리적)
    void deleteExpiredData(@Param("pivot") int pivot);
}
