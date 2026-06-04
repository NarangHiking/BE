package com.naranghiking.recommend.service;

import com.naranghiking.recommend.dao.RecommendDao;
import com.naranghiking.track.dto.Track;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService{

    private final RecommendDao recommendDao;

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
        return recommendDao.selectRecommends(userId);
    }
}
