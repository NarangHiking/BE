package com.naranghiking.trackComment.service;

import com.naranghiking.trackComment.dao.TrackCommentDao;
import com.naranghiking.trackComment.dto.TrackCommentListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackCommentServiceImpl implements TrackCommentService {

    private final TrackCommentDao trackCommentDao;

    @Override
    public List<TrackCommentListResponse> selectAll(Long trackId) {
        // 결과값이 0이어도 알아서 빈 리스트가 전달
        return trackCommentDao.selectAll(trackId);
    }
}
