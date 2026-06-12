package com.naranghiking.trackComment.dao;

import com.naranghiking.common.dto.ImageRequest;
import com.naranghiking.trackComment.dto.TrackCommentListResponse;
import com.naranghiking.trackComment.dto.TrackCommentRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TrackCommentDao {
    // 후기 한 건만 조회해서 userId 반환
    Long selectById(Long commentId);
    // 한 트랙에 대한 전체 후기 조회
    List<TrackCommentListResponse> selectAll(Long trackId);
    // 후기 작성
    int insert(TrackCommentRequest comment);
    // 후기 작성 시 이미지가 있을 경우 저장
    void insertImages(@Param("id") Long id, @Param("images") List<ImageRequest> saveImages);
    // 후기 수정
    int update(TrackCommentRequest comment);
    // 후기에 수정하면서 삭제될 이미지 처리
    void deleteImages(List<String> deletedImages);
    // 후기 논리적 삭제
    void delete(Long commentId);
    // 물리적 삭제를 위한 idList 조회
    List<Long> selectExpiredData(@Param("pivot") int pivot);
    // 삭제될 후기에 첨부된 이미지 리스트 조회
    List<String> selectExpiredImages(List<Long> idList);
    // 후기의 물리적 삭제 수행
    void deleteExpiredComments(List<Long> idList);
}
