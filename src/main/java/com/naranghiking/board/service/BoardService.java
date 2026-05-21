package com.naranghiking.board.service;

import com.naranghiking.board.dto.BoardResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {
    BoardResponse b1 = new BoardResponse(1, 2, "등산", "테스트", "free", LocalDateTime.now());
    BoardResponse b2 = new BoardResponse(2, 3, "게시글", "이번에는 건의", "feedback", LocalDateTime.now());

    public List<BoardResponse> selectAll() {
        List<BoardResponse> result = new ArrayList<>();
        result.add(b1);
        result.add(b2);
        return result;
    }
}
