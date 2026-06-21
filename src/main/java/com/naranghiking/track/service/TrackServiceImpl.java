package com.naranghiking.track.service;

import com.naranghiking.common.service.R2Service;
import com.naranghiking.track.dao.TrackDao;
import com.naranghiking.track.dto.Track;
import com.naranghiking.track.dto.TrackCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackServiceImpl implements TrackService{

    private final TrackDao trackDao;
    private final R2Service r2Service;

    // 조회된 트랙들에 GPX 공개 URL을 채워준다.
    private List<Track> withGpxUrl(List<Track> tracks) {
        tracks.forEach(t -> t.setGpxUrl(r2Service.getPublicUrl(t.getGpxFilePath())));
        return tracks;
    }

    @Override
    public List<Track> selectByMtnId(Long mountainId) {
        return withGpxUrl(trackDao.selectByMtnId(mountainId));
    }

    @Override
    public List<Track> selectByName(String name) {
        return withGpxUrl(trackDao.selectByName(name));
    }

    @Override
    public List<Track> selectByCondition(TrackCondition condition) {
        return withGpxUrl(trackDao.selectByCondition(condition));
    }

    @Override
    public Track selectById(Long id) {
        Track track = trackDao.selectById(id);
        if (track != null) {
            track.setGpxUrl(r2Service.getPublicUrl(track.getGpxFilePath()));
        }
        return track;
    }

    @Override
    public int insert(Track track) {
        return trackDao.insert(track);
    }

    @Override
    public int update(Track track) {
        return trackDao.update(track);
    }

    @Override
    public int delete(Long id) {
        return trackDao.delete(id);
    }
}
