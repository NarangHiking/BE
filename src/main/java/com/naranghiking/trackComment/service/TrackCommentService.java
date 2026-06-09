package com.naranghiking.trackComment.service;

import com.naranghiking.trackComment.dto.TrackCommentListResponse;
import com.naranghiking.trackComment.dto.TrackCommentRequest;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TrackCommentService {
    // 전체 후기 조회
    List<TrackCommentListResponse> selectAll(Long trackId);
    // 후기 작성
    void insert(@Valid TrackCommentRequest comment, List<MultipartFile> images);
}
