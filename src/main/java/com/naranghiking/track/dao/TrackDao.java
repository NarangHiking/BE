package com.naranghiking.track.dao;

import com.naranghiking.track.dto.Track;
import com.naranghiking.track.dto.TrackCondition;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TrackDao {

    // 산에 속하는 경로 검색
    List<Track> selectByMtnId(int mountainId);
    // 경로 이름으로 검색
    List<Track> selectByName(String name);
    // 복합 정보 검색(산 이름, 지역, 높이, 경로 이름)
    List<Track> selectByCondition(TrackCondition condition);
    // 마음에 드는 코스 보기
    List<Track> selectFavorites(int userId);
    // 추천한 코스 보기
    List<Track> selectRecommends(int userId);
    // 코스 ID로 찾기
    Track selectById(int id);

    int insert(Track track);
    int update(Track track);
    int delete(int id);
}
