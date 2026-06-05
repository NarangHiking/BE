package com.naranghiking.board.service;

import com.naranghiking.board.dto.BoardCommentRequest;
import com.naranghiking.board.dto.BoardCommentResponse;
import jakarta.validation.Valid;

public interface BoardCommentService {
    // 댓글 조회
    BoardCommentResponse selectById(Long id);
    // 댓글 생성
    BoardCommentResponse insert(Long boardId, @Valid BoardCommentRequest comment);
    // 댓글 수정
    BoardCommentResponse update(@Valid BoardCommentRequest comment);
    // 댓글 삭제(논리적)
    void delete(Long commentId, Long userId);
}
