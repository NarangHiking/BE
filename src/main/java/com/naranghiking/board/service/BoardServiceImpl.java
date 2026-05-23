package com.naranghiking.board.service;

import com.naranghiking.board.dao.BoardDao;
import com.naranghiking.board.dto.BoardRequest;
import com.naranghiking.board.dto.BoardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardDao boardDao;

    @Override
    public List<BoardResponse> selectAll(String keyword, String category) {
        // 키워드 또는 카테고리가 비어있을 때, 전부 null로 치환(동적 SQL로직에서 유리)
        if(keyword == null || keyword.trim().isEmpty()) keyword = null;
        if(category == null || category.trim().isEmpty()) category = null;

        return boardDao.selectAll(keyword, category);
    }

    @Override
    public BoardResponse selectById(Long id) {
        return boardDao.selectById(id);
    }

    @Override
    public int insert(BoardRequest board) {
        return boardDao.insert(board);
    }

    @Override
    public int update(Long id, BoardRequest board) {
        return boardDao.update(id, board);
    }
}
