package com.naranghiking.trackComment.service;

import com.naranghiking.common.dto.ImageRequest;
import com.naranghiking.common.service.FileService;
import com.naranghiking.trackComment.dao.TrackCommentDao;
import com.naranghiking.trackComment.dto.TrackCommentListResponse;
import com.naranghiking.trackComment.dto.TrackCommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TrackCommentServiceImpl implements TrackCommentService {

    private final TrackCommentDao trackCommentDao;
    private final FileService fileService;

    @Override
    public Long selectById(Long commentId) {
        Long result = trackCommentDao.selectById(commentId);
        if(result == null) throw new NoSuchElementException("조회 실패, 해당 후기가 존재하지 않습니다.");
        return result;
    }

    @Override
    public List<TrackCommentListResponse> selectAll(Long trackId) {
        // 결과값이 0이어도 알아서 빈 리스트가 전달
        return trackCommentDao.selectAll(trackId);
    }

    @Transactional
    @Override
    public void insert(TrackCommentRequest comment, List<MultipartFile> images) {
        int result = trackCommentDao.insert(comment);
        if(result == 0) throw new RuntimeException("후기 저장에 실패했습니다.");

        if(images != null && !images.isEmpty()) {
            List<ImageRequest> saveImages = fileService.saveFiles(images, "trackComment");

            if(!saveImages.isEmpty()) {
                trackCommentDao.insertImages(comment.getId(), saveImages);
            }
        }
    }

    @Transactional
    @Override
    public void update(TrackCommentRequest comment, List<MultipartFile> addedImages, List<String> deletedImages) {
        // getId로 해당 후기의 작성자 ID 가져오기
        Long selected = selectById(comment.getId());
        if(selected == null || !selected.equals(comment.getUserId())) { // 작성자와 수정자가 다르면 403 에러
            throw new AccessDeniedException("수정 권한이 없습니다. 본인의 후기만 수정 가능합니다.");
        }
        // 업데이트 수행
        int result = trackCommentDao.update(comment);
        if(result == 0) throw new RuntimeException("후기 수정 중 오류 발생");
        // 새로운 이미지 저장
        if(addedImages != null && !addedImages.isEmpty()) {
            List<ImageRequest> saveImages = fileService.saveFiles(addedImages, "trackComment");

            if(!saveImages.isEmpty()) {
                trackCommentDao.insertImages(comment.getId(), saveImages);
            }
        }
        // 기존 이미지 삭제
        if(deletedImages != null && !deletedImages.isEmpty()) {
            fileService.deleteFiles(deletedImages, "trackComment");
            // DB에서도 삭제
            trackCommentDao.deleteImages(deletedImages);
        }
    }

    @Transactional
    @Override
    public void delete(Long userId, Long commentId) {
        // commentId로 후기 가져오기
        Long selected = selectById(commentId);
        // comment.getUserId해서 userId와 맞는지 비교
        if(selected == null || !selected.equals(userId)) throw new AccessDeniedException("삭제 권한이 없습니다. 본인의 후기만 삭제 가능합니다.");
        // 맞으면 삭제 처리
        trackCommentDao.delete(commentId);
    }
}
