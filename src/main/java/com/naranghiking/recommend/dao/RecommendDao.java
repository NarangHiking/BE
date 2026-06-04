package com.naranghiking.recommend.dao;

import com.naranghiking.track.dto.Track;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecommendDao {

    // 추천
    int insert(Long userId, Long trackId);

    // 추천 취소
    int delete(Long userId, Long trackId);

    // 추천 확인 (토글 용도)
    boolean isExist(Long userId, Long trackId);

    List<Track> selectRecommends(Long userId);
}
