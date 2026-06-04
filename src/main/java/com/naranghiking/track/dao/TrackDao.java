package com.naranghiking.track.dao;

import com.naranghiking.track.dto.Track;
import com.naranghiking.track.dto.TrackCondition;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TrackDao {

    // 산에 속하는 경로 검색
    List<Track> selectByMtnId(Long mountainId);
    // 경로 이름으로 검색
    List<Track> selectByName(String name);
    // 복합 정보 검색(산 이름, 지역, 높이, 경로 이름)
    List<Track> selectByCondition(TrackCondition condition);

    // 코스 ID로 찾기
    Track selectById(Long id);

    int insert(Track track);
    int update(Track track);
    int delete(Long id);
}
