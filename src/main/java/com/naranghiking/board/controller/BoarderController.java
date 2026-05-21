package com.naranghiking.board.controller;

import com.naranghiking.board.dto.BoardResponse;
import com.naranghiking.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoarderController {

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<List<BoardResponse>> test() { // 테스트 확인 완료
        List<BoardResponse> result = boardService.selectAll();

        return ResponseEntity.ok(result);
    }
}
