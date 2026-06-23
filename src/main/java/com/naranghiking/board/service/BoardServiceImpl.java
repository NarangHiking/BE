package com.naranghiking.board.service;

import com.naranghiking.board.dao.BoardDao;
import com.naranghiking.board.dto.BoardDetailResponse;
import com.naranghiking.board.dto.BoardListResponse;
import com.naranghiking.board.dto.BoardRequest;
import com.naranghiking.common.dto.ImageRequest;
import com.naranghiking.common.service.R2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


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

    // 건의사항(feedback)은 어느 코스에 대한 글인지 알 수 있도록 trackId가 필수. (자유글 등은 null 허용)
    private void validateFeedbackTrack(BoardRequest board) {
        if ("feedback".equals(board.getCategory()) && board.getTrackId() == null) {
            throw new IllegalArgumentException("건의사항은 대상 등산 코스(trackId)를 선택해야 합니다.");
        }
    }

//    private List<ImageRequest> uploadImages(List<MultipartFile> images) {
//        List<ImageRequest> result = new ArrayList<>();
//        for (MultipartFile file : images) {
//            if (file.isEmpty()) continue;
//            try {
//                String key = r2Service.uploadImage(file, IMAGE_FOLDER);
//                result.add(new ImageRequest(file.getOriginalFilename(), key));
//            } catch (IOException e) {
//                throw new RuntimeException("이미지 업로드 중 에러 발생", e);
//            }
//        }
//        return result;
//    }
    private List<ImageRequest> uploadImages(List<MultipartFile> images, Long boardId) {
        List<ImageRequest> uploaded = new ArrayList<>();
        for (MultipartFile file : images) {
            if (file.isEmpty()) continue;
            try {
                String storedName = r2Service.uploadImage(file, IMAGE_FOLDER + "/" + boardId);
                uploaded.add(new ImageRequest(file.getOriginalFilename(), storedName));
            } catch (IOException e) {
                for (ImageRequest img : uploaded) {
                    try { r2Service.deleteFile(img.getStoredFilename()); } catch (Exception ignored) {}
                }
                throw new RuntimeException("이미지 업로드 중 에러 발생", e);
            }
        }
        return uploaded;
    }


    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
            .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }

    @Override
    public List<BoardListResponse> selectAll(String keyword, String category) {
        if(keyword == null || keyword.trim().isEmpty()) keyword = null;
        if(category == null || category.trim().isEmpty()) category = null;

        List<BoardListResponse> boards = boardDao.selectAll(keyword, category);
        if(boards != null) {
            boards.forEach(b -> {
                String image = b.getImage();
                b.setImageUrl("default_image.png".equals(image) ? null : r2Service.getPublicUrl(image));
            });
        }
        return boards;
    }

    @Override
    public BoardDetailResponse selectById(Long id) {
        BoardDetailResponse board = boardDao.selectById(id);
        if(board == null) throw new NoSuchElementException("조회 실패, 해당 게시글을 찾을 수 없습니다.");
        if(board.getImages() != null) {
            board.setImageUrls(board.getImages().stream()
                    .map(r2Service::getPublicUrl)
                    .toList());
        }
        return board;
    }

    @Transactional
    @Override
    public void insert(BoardRequest board, List<MultipartFile> images) {
        validateFeedbackTrack(board);
        int result = boardDao.insert(board);
        if(result == 0) throw new RuntimeException("게시글 저장에 실패했습니다.");

        if(images != null && !images.isEmpty()) {
            List<ImageRequest> saveImages = uploadImages(images, board.getId());

            if(!saveImages.isEmpty()) { // 성공적으로 값이 전달되면 DB에 저장
                boardDao.insertImages(board.getId(), saveImages);
            }
        }
    }

    @Transactional // 실패하면 롤백
    @Override
    public BoardDetailResponse update(Long id, BoardRequest board, List<MultipartFile> addedImages, List<String> deletedImages) {
        BoardDetailResponse selected = selectById(id); // null이면 알아서 에러 처리됨
        if(!selected.getUserId().equals(board.getUserId())) { // 게시글 작성자와 수정 요청자가 다르면 403 에러 발생 (Long 은 equals 로 비교)
            throw new AccessDeniedException("수정 권한이 없습니다. 본인의 게시글만 수정이 가능합니다.");
        }
        validateFeedbackTrack(board);

        int result = boardDao.update(id, board);
        if(result == 0) throw new RuntimeException("게시글 수정 중 오류 발생");
        if(addedImages != null && !addedImages.isEmpty()) {
            List<ImageRequest> saveImages = uploadImages(addedImages, board.getId());
            if(!saveImages.isEmpty()) {
                boardDao.insertImages(id, saveImages);
            }
        }
        // 기존 이미지 삭제
        if(deletedImages != null && !deletedImages.isEmpty()) {
            deletedImages.forEach(r2Service::deleteFile); // R2 저장소에서 삭제
            boardDao.deleteImages(deletedImages);
        }

        return selectById(id); // 수정된 게시글 다시 조회해서 리턴
    }

    @Transactional // 실패하면 롤백
    @Override
    public void deleteById(Long id, Long userId) {
        BoardDetailResponse selected = selectById(id);
        if(!selected.getUserId().equals(userId) && !isAdmin()) { // 게시글 작성자와 수정 요청자가 다르면 403 에러 발생 (Long 은 equals 로 비교)
            throw new AccessDeniedException("삭제 권한이 없습니다. 본인의 게시글만 수정이 가능합니다.");
        }

        int result = boardDao.deleteById(id);
        if(result == 0) throw new RuntimeException("게시글 삭제 중 오류 발생");
    }
}
