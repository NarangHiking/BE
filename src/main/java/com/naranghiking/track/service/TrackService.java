package com.naranghiking.track.service;

import com.naranghiking.track.dto.Track;
import com.naranghiking.track.dto.TrackCondition;

import java.util.List;

public interface TrackService {

    List<Track> selectByMtnId(int mountainId);

    List<Track> selectByName(String name);

    List<Track> selectByCondition(TrackCondition condition);

    Track selectById(int id);

    int insert(Track track);

    int update(Track track);

    int delete(int id);
}
