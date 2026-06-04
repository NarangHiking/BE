package com.naranghiking.mypage.service;

import com.naranghiking.track.dto.Track;

import java.util.List;

public interface MyPageService {
    List<Track> selectFavorites(int userId);

    public List<Track> selectRecommends(int userId);
}
