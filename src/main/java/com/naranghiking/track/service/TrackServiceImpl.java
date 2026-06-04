package com.naranghiking.track.service;

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

    @Override
    public List<Track> selectByMtnId(int mountainId) {
        return trackDao.selectByMtnId(mountainId);
    }

    @Override
    public List<Track> selectByName(String name) {
        return trackDao.selectByName(name);
    }

    @Override
    public List<Track> selectByCondition(TrackCondition condition) {
        return trackDao.selectByCondition(condition);
    }

    @Override
    public Track selectById(int id) {
        return trackDao.selectById(id);
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
    public int delete(int id) {
        return trackDao.delete(id);
    }
}
