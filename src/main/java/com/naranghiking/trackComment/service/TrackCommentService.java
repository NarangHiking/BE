package com.naranghiking.trackComment.service;

import com.naranghiking.trackComment.dto.TrackCommentListResponse;

import java.util.List;

public interface TrackCommentService {
    // 전체 후기 조회
    List<TrackCommentListResponse> selectAll(Long trackId);
}
