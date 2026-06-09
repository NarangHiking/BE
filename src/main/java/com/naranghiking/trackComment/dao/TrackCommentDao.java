package com.naranghiking.trackComment.dao;

import com.naranghiking.trackComment.dto.TrackCommentListResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TrackCommentDao {
    // 한 트랙에 대한 전체 후기 조회
    List<TrackCommentListResponse> selectAll(Long trackId);
}
