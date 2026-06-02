package com.naranghiking.board.service;

import com.naranghiking.board.dao.BoardDao;
import com.naranghiking.board.dto.BoardDetailResponse;
import com.naranghiking.board.dto.BoardListResponse;
import com.naranghiking.board.dto.BoardRequest;
import com.naranghiking.board.dto.BoardResponse;
import com.naranghiking.common.dto.ImageRequest;
import com.naranghiking.common.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardDao boardDao;
    private final FileService fileService;

    @Override
    public List<BoardListResponse> selectAll(String keyword, String category) {
        // 키워드 또는 카테고리가 비어있을 때, 전부 null로 치환(동적 SQL로직에서 유리)
        if(keyword == null || keyword.trim().isEmpty()) keyword = null;
        if(category == null || category.trim().isEmpty()) category = null;

        return boardDao.selectAll(keyword, category);
    }

    @Override
    public BoardDetailResponse selectById(Long id) {
        BoardDetailResponse board = boardDao.selectById(id);
        // 게시글을 찾지 못하면 404 반환
        if(board == null) throw new NoSuchElementException("조회 실패, 해당 게시글을 찾을 수 없습니다.");
        return board;
    }

    @Transactional // 실패하면 롤백
    @Override
    public void insert(BoardRequest board, List<MultipartFile> images) {
        int result = boardDao.insert(board);
        if(result == 0) throw new RuntimeException("게시글 저장에 실패했습니다.");

        if(images != null && !images.isEmpty()) {
            List<ImageRequest> newImages = fileService.saveFiles(images, "board");

            if(!newImages.isEmpty()) { // 성공적으로 값이 전달되면 DB에 저장
                boardDao.insertImages(board.getId(), newImages);
            }
        }
    }

    @Transactional // 실패하면 롤백
    @Override
    public BoardResponse update(Long id, BoardRequest board) {
        selectById(id); // null이면 알아서 에러 처리됨

        int result = boardDao.update(id, board);
        if(result == 0) throw new RuntimeException("게시글 수정 중 오류 발생");

//        return selectById(id); // 수정된 게시글 다시 조회해서 리턴
        return null;
    }

    @Transactional // 실패하면 롤백
    @Override
    public void deleteById(Long id) {
        selectById(id);

        int result = boardDao.deleteById(id);
        if(result == 0) throw new RuntimeException("게시글 삭제 중 오류 발생");
    }
}
