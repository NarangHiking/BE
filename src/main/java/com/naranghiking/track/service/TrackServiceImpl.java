package com.naranghiking.track.service;

import com.naranghiking.common.service.R2Service;
import com.naranghiking.track.dao.TrackDao;
import com.naranghiking.track.dto.Track;
import com.naranghiking.track.dto.TrackCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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

    @Transactional
    @Override
    public List<Track> bulkInsert(List<Track> tracks, List<MultipartFile> files) {
        if (tracks == null || tracks.isEmpty()) {
            throw new IllegalArgumentException("등록할 코스가 없습니다.");
        }
        List<String> uploadedKeys = new ArrayList<>(); // 롤백 시 R2 정리를 위해 추적
        try {
            for (int i = 0; i < tracks.size(); i++) {
                Track t = tracks.get(i);
                MultipartFile file = (files != null && i < files.size()) ? files.get(i) : null;
                if (file != null && !file.isEmpty()) {
                    String key = r2Service.uploadFile(file, "gpx/" + t.getMountainId());
                    t.setGpxFilePath(key);
                    uploadedKeys.add(key);
                }
                trackDao.insert(t);
                t.setGpxUrl(r2Service.getPublicUrl(t.getGpxFilePath()));
            }
            return tracks;
        } catch (Exception e) {
            uploadedKeys.forEach(r2Service::deleteFile);
            throw new RuntimeException("코스 일괄 등록 실패로 전체 롤백되었습니다.", e);
        }
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
