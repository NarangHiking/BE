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
    public List<BoardResponse> selectAll() {
        return boardDao.selectAll();
    }

    @Override
    public BoardResponse selectById(long id) {
        return boardDao.selectById(id);
    }

    @Override
    public int insert(BoardRequest board) {
        return boardDao.insert(board);
    }
}
