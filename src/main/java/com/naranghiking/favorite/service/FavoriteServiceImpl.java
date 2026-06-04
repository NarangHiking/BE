package com.naranghiking.favorite.service;

import com.naranghiking.favorite.dao.FavoriteDao;
import com.naranghiking.track.dto.Track;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteDao favoriteDao;

    @Override
    public int insert(Long userId, Long trackId) {
        System.out.println("FavoriteService.insert 호출 - userId: " + userId + ", trackId: " + trackId);
        boolean exists = favoriteDao.isExist(userId, trackId);
        System.out.println("exists: " + exists);
        return favoriteDao.insert(userId, trackId);
    }

    @Override
    public int delete(Long userId, Long trackId) {
        return favoriteDao.delete(userId, trackId);
    }

    @Override
    public boolean isExist(Long userId, Long trackId) {
        return favoriteDao.isExist(userId, trackId);
    }

    @Override
    public List<Track> selectFavorites(Long userId) {
        return favoriteDao.selectFavorites(userId);
    }
}
