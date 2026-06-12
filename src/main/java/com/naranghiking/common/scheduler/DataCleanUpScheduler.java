package com.naranghiking.common.scheduler;

import com.naranghiking.board.dao.BoardCommentDao;
import com.naranghiking.board.dao.BoardDao;
import com.naranghiking.common.service.FileService;
import com.naranghiking.trackComment.dao.TrackCommentDao;
import com.naranghiking.user.dao.UserDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataCleanUpScheduler { // 테스트는 10초마다, 1분이 지난 데이터 삭제

    final int EXPIRED_TIME = 1; // [1 = 1분], [1 * 60 * 24 * 90 = 90일]

    private final BoardDao boardDao;
    private final BoardCommentDao boardCommentDao;
    private final FileService fileService;
    private final UserDao userDao;
    private final TrackCommentDao trackCommentDao;

    @Scheduled(fixedDelay = 60_000) // 60초마다 수행
//    @Scheduled(cron = "0 0 3 * * *") // 초 분 시 일 월 요일
    @Transactional
    public void cleanUpBoard() {
        log.info("[Board 스케줄러 시작] 논리적 삭제로 이루어졌던 데이터들 물리적 삭제 실시!!!!!!!");

        // 게시글 댓글 삭제
        boardCommentDao.deleteExpiredData(EXPIRED_TIME);
        log.info("[Board 스케줄러 동작 중] 게시글 댓글 삭제 완료");

        // 삭제할 게시글의 id 리스트 조회 > 리스트 기반으로 stored_filename 리스트 조회 > 이미지 삭제 > 게시글 삭제
        List<Long> idList = boardDao.selectExpiredData(EXPIRED_TIME);
        log.info("[Board 스케줄러 동작 중] 삭제할 게시글의 id 리스트 {}건 조회 완료", (idList != null) ? idList.size() : 0);
        if(idList != null && !idList.isEmpty()) {
            List<String> deletedImageList = boardDao.selectExpiredImages(idList);
            log.info("[Board 스케줄러 동작 중] 삭제할 이미지의 stored_filename 리스트 {}건 조회 완료", (deletedImageList != null) ? deletedImageList.size() : 0);

            if(deletedImageList != null && !deletedImageList.isEmpty()) {
                boardDao.deleteImages(deletedImageList); // DB에서 이미지 삭제
                log.info("[Board 스케줄러 동작 중] 게시글에 첨부된 이미지 DB에서 삭제 완료");
                fileService.deleteFiles(deletedImageList, "board"); // 저장소에서 이미지 삭제
                log.info("[Board 스케줄러 동작 중] 게시글에 첨부된 이미지 저장소에서 삭제 완료");
            }

            boardDao.deleteExpiredBoards(idList); // 게시글 삭제
            log.info("[Board 스케줄러 동작 중] 게시글 삭제 완료");
        }
        log.info("[Board 스케줄러 종료] 게시글 관련 삭제 완료");
    }

    @Scheduled(fixedDelay = 60_000)
//    @Scheduled(cron = "0 0 3 * * *") // 초 분 시 일 월 요일
    @Transactional
    public void cleanUpTrackComment() {
        log.info("[TrackComment 스케줄러 시작] 논리적 삭제로 이루어졌던 데이터들 물리적 삭제 실시!!!!!!!");

        // 삭제할 후기의 id 리스트 조회 > 리스트 기반으로 stored_filename 리스트 조회 > 이미지 삭제 > 후기 삭제
        List<Long> idList = trackCommentDao.selectExpiredData(EXPIRED_TIME);
        log.info("[TrackComment 스케줄러 동작 중] 삭제할 후기의 id 리스트 {}건 조회 완료", (idList != null) ? idList.size() : 0);
        if(idList != null && !idList.isEmpty()) {
            List<String> deletedImageList = trackCommentDao.selectExpiredImages(idList);
            log.info("[TrackComment 스케줄러 동작 중] 삭제할 이미지의 stored_filename 리스트 {}건 조회 완료", (deletedImageList != null) ? deletedImageList.size() : 0);

            if(deletedImageList != null && !deletedImageList.isEmpty()) {
                trackCommentDao.deleteImages(deletedImageList);
                log.info("[TrackComment 스케줄러 동작 중] 후기에 첨부된 이미지 DB에서 삭제 완료");
                fileService.deleteFiles(deletedImageList, "trackComment");
                log.info("[TrackComment 스케줄러 동작 중] 후기에 첨부된 이미지 저장소에서 삭제 완료");
            }

            trackCommentDao.deleteExpiredComments(idList);
            log.info("[TrackComment 스케줄러 동작 중] 후기 삭제 완료");
        }
        log.info("[TrackComment 스케줄러 종료] 후기 관련 삭제 완료");
    }

    @Scheduled(fixedDelay = 60_000)
//    @Scheduled(cron = "0 0 3 * * *") // 초 분 시 일 월 요일
    @Transactional
    public void cleanUpUser() {
        log.info("[User 스케줄러 시작] 논리적 삭제로 이루어졌던 데이터들 물리적 삭제 실시!!!!!!!");
        userDao.deleteExpiredUsers(EXPIRED_TIME); // 유저 삭제
        log.info("[User 스케줄러 종료] 유저 정보 삭제 완료");
    }

}
