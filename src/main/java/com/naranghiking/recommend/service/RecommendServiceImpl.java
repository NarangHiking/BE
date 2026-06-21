package com.naranghiking.recommend.service;

import com.naranghiking.common.service.R2Service;
import com.naranghiking.recommend.dao.RecommendDao;
import com.naranghiking.track.dto.Track;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService{

    private final RecommendDao recommendDao;
    private final R2Service r2Service;

    @Override
    public int insert(Long userId, Long trackId) {
        return recommendDao.insert(userId, trackId);
    }

    @Override
    public int delete(Long userId, Long trackId) {
        return recommendDao.delete(userId, trackId);
    }

    @Override
    public boolean isExist(Long userId, Long trackId) {
        return recommendDao.isExist(userId, trackId);
    }

    @Override
    public List<Track> selectRecommends(Long userId) {
        List<Track> tracks = recommendDao.selectRecommends(userId);
        tracks.forEach(t -> t.setGpxUrl(r2Service.getPublicUrl(t.getGpxFilePath())));
        return tracks;
    }
}
