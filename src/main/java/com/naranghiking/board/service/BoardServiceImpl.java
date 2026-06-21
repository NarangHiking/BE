package com.naranghiking.board.service;

import com.naranghiking.board.dao.BoardDao;
import com.naranghiking.board.dto.BoardDetailResponse;
import com.naranghiking.board.dto.BoardListResponse;
import com.naranghiking.board.dto.BoardRequest;
import com.naranghiking.common.dto.ImageRequest;
import com.naranghiking.common.service.R2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private static final String IMAGE_FOLDER = "board";

    private final BoardDao boardDao;
    private final R2Service r2Service;

    // 게시글 이미지들을 R2에 업로드하고 (원본명, R2 키) 목록을 만든다.
    private List<ImageRequest> uploadImages(List<MultipartFile> images) {
        List<ImageRequest> result = new ArrayList<>();
        for (MultipartFile file : images) {
            if (file.isEmpty()) continue;
            try {
                String key = r2Service.uploadImage(file, IMAGE_FOLDER);
                result.add(new ImageRequest(file.getOriginalFilename(), key));
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 중 에러 발생", e);
            }
        }
        return result;
    }

    @Override
    public List<BoardListResponse> selectAll(String keyword, String category) {
        // 키워드 또는 카테고리가 비어있을 때, 전부 null로 치환(동적 SQL로직에서 유리)
        if(keyword == null || keyword.trim().isEmpty()) keyword = null;
        if(category == null || category.trim().isEmpty()) category = null;

        List<BoardListResponse> boards = boardDao.selectAll(keyword, category);
        // 대표 이미지 키를 화면 표시용 공개 URL로 변환 (이미지가 없으면 null로 두어 FE가 기본 이미지로 대체)
        if(boards != null) {
            boards.forEach(b -> {
                String image = b.getImage(); // 이미지가 없으면 "default_image.png" 반환
                b.setImageUrl("default_image.png".equals(image) ? null : r2Service.getPublicUrl(image));
            });
        }
        return boards;
    }

    @Override
    public BoardDetailResponse selectById(Long id) {
        BoardDetailResponse board = boardDao.selectById(id);
        // 게시글을 찾지 못하면 404 반환
        if(board == null) throw new NoSuchElementException("조회 실패, 해당 게시글을 찾을 수 없습니다.");
        // 저장된 이미지 키들을 화면 표시용 공개 URL로 변환
        if(board.getImages() != null) {
            board.setImageUrls(board.getImages().stream()
                    .map(r2Service::getPublicUrl)
                    .toList());
        }
        return board;
    }

    @Transactional // 실패하면 롤백
    @Override
    public void insert(BoardRequest board, List<MultipartFile> images) {
        int result = boardDao.insert(board);
        if(result == 0) throw new RuntimeException("게시글 저장에 실패했습니다.");

        if(images != null && !images.isEmpty()) {
            List<ImageRequest> saveImages = uploadImages(images);

            if(!saveImages.isEmpty()) { // 성공적으로 값이 전달되면 DB에 저장
                boardDao.insertImages(board.getId(), saveImages);
            }
        }
    }

    @Transactional // 실패하면 롤백
    @Override
    public BoardDetailResponse update(Long id, BoardRequest board, List<MultipartFile> addedImages, List<String> deletedImages) {
        BoardDetailResponse selected = selectById(id); // null이면 알아서 에러 처리됨
        if(selected.getUserId() != board.getUserId()) { // 게시글 작성자와 수정 요청자가 다르면 403 에러 발생
            throw new AccessDeniedException("수정 권한이 없습니다. 본인의 게시글만 수정이 가능합니다.");
        }
        // 게시글 수정
        int result = boardDao.update(id, board);
        if(result == 0) throw new RuntimeException("게시글 수정 중 오류 발생");
        // 새로운 이미지 저장
        if(addedImages != null && !addedImages.isEmpty()) {
            List<ImageRequest> saveImages = uploadImages(addedImages);

            if(!saveImages.isEmpty()) {
                boardDao.insertImages(id, saveImages);
            }
        }
        // 기존 이미지 삭제
        if(deletedImages != null && !deletedImages.isEmpty()) {
            deletedImages.forEach(r2Service::deleteFile); // R2 저장소에서 삭제
            // DB에서도 삭제
            boardDao.deleteImages(deletedImages);
        }

        return selectById(id); // 수정된 게시글 다시 조회해서 리턴
    }

    @Transactional // 실패하면 롤백
    @Override
    public void deleteById(Long id, Long userId) {
        BoardDetailResponse selected = selectById(id);
        if(selected.getUserId() != userId) { // 게시글 작성자와 수정 요청자가 다르면 403 에러 발생
            throw new AccessDeniedException("수정 권한이 없습니다. 본인의 게시글만 수정이 가능합니다.");
        }

        int result = boardDao.deleteById(id);
        if(result == 0) throw new RuntimeException("게시글 삭제 중 오류 발생");
    }
}
