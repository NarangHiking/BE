package com.naranghiking.trackComment.dao;

import com.naranghiking.common.dto.ImageRequest;
import com.naranghiking.trackComment.dto.TrackCommentListResponse;
import com.naranghiking.trackComment.dto.TrackCommentRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TrackCommentDao {
    // 한 트랙에 대한 전체 후기 조회
    List<TrackCommentListResponse> selectAll(Long trackId);
    // 후기 작성
    int insert(TrackCommentRequest comment);
    // 후기 작성 시 이미지가 있을 경우 저장
    void insertImages(@Param("id") Long id, @Param("images") List<ImageRequest> saveImages);
}
