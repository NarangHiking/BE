package com.naranghiking.trackComment.service;

import com.naranghiking.trackComment.dto.TrackCommentListResponse;
import com.naranghiking.trackComment.dto.TrackCommentRequest;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TrackCommentService {
    // 후기 한 건만 조회해서 userId 반환
    Long selectById(Long commentId);
    // 전체 후기 조회
    List<TrackCommentListResponse> selectAll(Long trackId);
    // 후기 작성
    void insert(@Valid TrackCommentRequest comment, List<MultipartFile> images);
    // 후기 삭제
    void delete(Long userId, Long commentId);
    // 후기 수정
    void update(@Valid TrackCommentRequest comment, List<MultipartFile> addedImages, List<String> deletedImages);
    // 후기 내용만 수정 (이미지 없이 인라인 텍스트 수정용)
    void updateContent(@Valid TrackCommentRequest comment);
}
