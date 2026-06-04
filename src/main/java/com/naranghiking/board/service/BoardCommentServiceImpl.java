package com.naranghiking.board.service;

import com.naranghiking.board.dao.BoardCommentDao;
import com.naranghiking.board.dto.BoardCommentRequest;
import com.naranghiking.board.dto.BoardCommentResponse;
import lombok.RequiredArgsConstructor;
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
}
