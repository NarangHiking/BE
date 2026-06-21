package com.naranghiking.trackComment.service;

import com.naranghiking.common.dto.ImageRequest;
import com.naranghiking.common.dto.ImageResponse;
import com.naranghiking.common.service.R2Service;
import com.naranghiking.trackComment.dao.TrackCommentDao;
import com.naranghiking.trackComment.dto.TrackCommentListResponse;
import com.naranghiking.trackComment.dto.TrackCommentRequest;
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
public class TrackCommentServiceImpl implements TrackCommentService {

    private static final String IMAGE_FOLDER = "trackComment";

    private final TrackCommentDao trackCommentDao;
    private final R2Service r2Service;

    // 후기 이미지들을 R2에 업로드하고 (원본명, R2 키) 목록을 만든다.
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
    public Long selectById(Long commentId) {
        Long result = trackCommentDao.selectById(commentId);
        if(result == null) throw new NoSuchElementException("조회 실패, 해당 후기가 존재하지 않습니다.");
        return result;
    }

    @Override
    public List<TrackCommentListResponse> selectAll(Long trackId) {
        // 결과값이 0이어도 알아서 빈 리스트가 전달
        List<TrackCommentListResponse> comments = trackCommentDao.selectAll(trackId);
        // 각 후기 이미지 키들을 화면 표시용 공개 URL로 변환
        comments.forEach(c -> {
            if (c.getImages() != null) {
                c.setImageUrls(c.getImages().stream()
                        .map(ImageResponse::storedFilename)
                        .map(r2Service::getPublicUrl)
                        .toList());
            }
        });
        return comments;
    }

    @Transactional
    @Override
    public void insert(TrackCommentRequest comment, List<MultipartFile> images) {
        int result = trackCommentDao.insert(comment);
        if(result == 0) throw new RuntimeException("후기 저장에 실패했습니다.");

        if(images != null && !images.isEmpty()) {
            List<ImageRequest> saveImages = uploadImages(images);

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
            List<ImageRequest> saveImages = uploadImages(addedImages);

            if(!saveImages.isEmpty()) {
                trackCommentDao.insertImages(comment.getId(), saveImages);
            }
        }
        // 기존 이미지 삭제
        if(deletedImages != null && !deletedImages.isEmpty()) {
            deletedImages.forEach(r2Service::deleteFile); // R2 저장소에서 삭제
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
