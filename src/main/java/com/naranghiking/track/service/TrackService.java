package com.naranghiking.track.service;

import com.naranghiking.track.dto.Track;
import com.naranghiking.track.dto.TrackCondition;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TrackService {

    List<Track> selectByMtnId(Long mountainId);

    List<Track> selectByName(String name);

    List<Track> selectByCondition(TrackCondition condition);

    Track selectById(Long id);

    int insert(Track track);

    // 여러 코스를 한 번에 등록 (전부 성공/전부 롤백). files[i] ↔ tracks[i] 순서로 매칭, 비거나 없으면 GPX 없이 등록
    List<Track> bulkInsert(List<Track> tracks, List<MultipartFile> files);

    int update(Track track);

    int delete(Long id);
}
