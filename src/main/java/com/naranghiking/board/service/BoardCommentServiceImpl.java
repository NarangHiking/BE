package com.naranghiking.board.service;

import com.naranghiking.board.dao.BoardCommentDao;
import com.naranghiking.board.dto.BoardCommentRequest;
import com.naranghiking.board.dto.BoardCommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BoardCommentServiceImpl implements BoardCommentService {

    private final BoardCommentDao boardCommentDao;


    @Override
    public BoardCommentResponse selectById(Long id) {
        BoardCommentResponse comment = boardCommentDao.selectById(id);
        // 댓글이 없을 때는 404
        if(comment == null) throw new NoSuchElementException("조회 실패, 해당 댓글은 존재하지 않습니다.");
        return comment;
    }

    @Transactional
    @Override
    public BoardCommentResponse insert(Long boardId, BoardCommentRequest comment) {
        int result = boardCommentDao.insert(boardId, comment);
        if(result == 0) throw new RuntimeException("댓글 저장에 실패했습니다.");
        return selectById(comment.getId());
    }

    @Transactional
    @Override
    public BoardCommentResponse update(BoardCommentRequest comment) {
        BoardCommentResponse selected = selectById(comment.getId()); // 댓글이 존재하지 않으면 알아서 404
        if(selected.getUserId() != comment.getUserId()) { // userId 불일치 > 403 에러
            throw new AccessDeniedException("수정 권한이 없습니다. 본인의 댓글만 수정 가능합니다.");
        }

        int result = boardCommentDao.update(comment);
        if(result == 0) throw new RuntimeException("댓글 수정에 실패했습니다.");
        return selectById(comment.getId());
    }

    @Transactional
    @Override
    public void delete(Long commentId, Long userId) {
        BoardCommentResponse selected = selectById(commentId); // 댓글이 존재하지 않으면 알아서 404
        if(selected.getUserId() != userId) { // userId 불일치 > 403 에러
            throw new AccessDeniedException("수정 권한이 없습니다. 본인의 댓글만 수정 가능합니다.");
        }

        int result = boardCommentDao.delete(commentId);
        System.out.println(result);
        if(result == 0) throw new RuntimeException("댓글 삭제에 실패했습니다.");
    }
}
