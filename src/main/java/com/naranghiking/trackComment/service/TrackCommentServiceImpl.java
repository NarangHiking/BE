package com.naranghiking.trackComment.service;

import com.naranghiking.common.dto.ImageRequest;
import com.naranghiking.common.service.FileService;
import com.naranghiking.trackComment.dao.TrackCommentDao;
import com.naranghiking.trackComment.dto.TrackCommentListResponse;
import com.naranghiking.trackComment.dto.TrackCommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackCommentServiceImpl implements TrackCommentService {

    private final TrackCommentDao trackCommentDao;
    private final FileService fileService;

    @Override
    public List<TrackCommentListResponse> selectAll(Long trackId) {
        // 결과값이 0이어도 알아서 빈 리스트가 전달
        return trackCommentDao.selectAll(trackId);
    }

    @Transactional
    @Override
    public void insert(TrackCommentRequest comment, List<MultipartFile> images) {
        int result = trackCommentDao.insert(comment);
        if(result == 0) throw new RuntimeException("후기 저장에 실패했습니다.");

        if(images != null && !images.isEmpty()) {
            List<ImageRequest> saveImages = fileService.saveFiles(images, "trackComment");

            if(!saveImages.isEmpty()) {
                trackCommentDao.insertImages(comment.getId(), saveImages);
            }
        }
    }
}
