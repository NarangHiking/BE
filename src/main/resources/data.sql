
/*
데이터 삽입 순서
1. users
2. mountains
3. tracks
4. boards
5. boards_images
6. board_comments
7. recommends / favorites

 */

-- users 테이블 더미데이터
INSERT INTO users (email, pass, name, role) VALUES
    ('hwc@admin.com', '$2a$10$BuGl5LRUoahZLlUlh2oWR.mcrDmEoTdXSi1bDp3VDL5z5EqY1VKmK', '관리자', 'ADMIN'),
    ('psj@admin.com', '$2a$10$BuGl5LRUoahZLlUlh2oWR.mcrDmEoTdXSi1bDp3VDL5z5EqY1VKmK', '관리자', 'ADMIN'),;
    -- ,
    -- ('hong@test.com', '$2a$10$BuGl5LRUoahZLlUlh2oWR.mcrDmEoTdXSi1bDp3VDL5z5EqY1VKmK', '홍길동', 'USER'),
    -- ('kim@test.com', '$2a$10$BuGl5LRUoahZLlUlh2oWR.mcrDmEoTdXSi1bDp3VDL5z5EqY1VKmK', '김철수', 'USER'),
    -- ('lee@test.com', '$2a$10$BuGl5LRUoahZLlUlh2oWR.mcrDmEoTdXSi1bDp3VDL5z5EqY1VKmK', '이영희', 'USER'),
    -- ('park@test.com', '$2a$10$BuGl5LRUoahZLlUlh2oWR.mcrDmEoTdXSi1bDp3VDL5z5EqY1VKmK', '박민수', 'USER'),
    -- ('choi@test.com', '$2a$10$BuGl5LRUoahZLlUlh2oWR.mcrDmEoTdXSi1bDp3VDL5z5EqY1VKmK', '최지우', 'USER'),
    -- ('jung@test.com', '$2a$10$BuGl5LRUoahZLlUlh2oWR.mcrDmEoTdXSi1bDp3VDL5z5EqY1VKmK', '정수진', 'USER'),
    -- ('kang@test.com', '$2a$10$BuGl5LRUoahZLlUlh2oWR.mcrDmEoTdXSi1bDp3VDL5z5EqY1VKmK', '강현우', 'USER'),
    -- ('yoon@test.com', '$2a$10$BuGl5LRUoahZLlUlh2oWR.mcrDmEoTdXSi1bDp3VDL5z5EqY1VKmK', '서연', 'USER'),
    -- ('oh@test.com', '$2a$10$BuGl5LRUoahZLlUlh2oWR.mcrDmEoTdXSi1bDp3VDL5z5EqY1VKmK', '오재훈', 'ADMIN');

-- INSERT INTO users (email, pass, name, role, deleted_at) VALUES
--     ('deleted@test.com', '$2a$10$BuGl5LRUoahZLlUlh2oWR.mcrDmEoTdXSi1bDp3VDL5z5EqY1VKmK', '탈퇴유저', 'USER', '2026-04-01 10:00:00');

-- INSERT INTO mountains (name, location, height, description, original_filename, stored_filename) VALUES
--     ('북한산', '서울/경기', 836, '서울 근교 대표 명산', 'bukhansan.jpg', 'bukhansan_stored.jpg'),
--     ('설악산', '강원도', 1708, '강원도 대표 명산', 'seorak.jpg', 'seorak_stored.jpg'),
--     ('지리산', '전남/경남', 1915, '남한 최고봉', 'jiri.jpg', 'jiri_stored.jpg'),
--     ('한라산', '제주도', 1950, '제주도 최고봉', 'halla.jpg', 'halla_stored.jpg'),
--     ('오대산', '강원도', 1563, '강원도 명산', 'odae.jpg', 'odae_stored.jpg');

-- 트랙 더미 데이터
-- INSERT INTO tracks (mountain_id, name, gpx_file_path) VALUES
--     -- 북한산 (id: 1)
--     (1, '북한산성 코스', '/gpx/temp/track1.gpx'),
--     (1, '우이령 코스', '/gpx/temp/track2.gpx'),
--     (1, '도선사 코스', '/gpx/temp/track3.gpx'),
--     (1, '진달래능선 코스', '/gpx/temp/track4.gpx'),

--     -- 설악산 (id: 2)
--     (2, '대청봉 코스', '/gpx/temp/track5.gpx'),
--     (2, '오색 코스', '/gpx/temp/track6.gpx'),
--     (2, '천불동계곡 코스', '/gpx/temp/track7.gpx'),
--     (2, '울산바위 코스', '/gpx/temp/track8.gpx'),

--     -- 지리산 (id: 3)
--     (3, '천왕봉 코스', '/gpx/temp/track9.gpx'),
--     (3, '노고단 코스', '/gpx/temp/track10.gpx'),
--     (3, '뱀사골 코스', '/gpx/temp/track11.gpx'),

--     -- 한라산 (id: 4)
--     (4, '성판악 코스', '/gpx/temp/track12.gpx'),
--     (4, '관음사 코스', '/gpx/temp/track13.gpx'),
--     (4, '어리목 코스', '/gpx/temp/track14.gpx'),

--     -- 오대산 (id: 5)
--     (5, '비로봉 코스', '/gpx/temp/track15.gpx'),
--     (5, '상원사 코스', '/gpx/temp/track16.gpx');

-- -- boards 테이블 더미데이터
-- INSERT INTO boards(user_id, title, content, category)
-- VALUES (1, "등산", "테스트", "free");

-- INSERT INTO boards(user_id, track_id, title, content, category)
-- VALUES (2, 3, "게시글", "이번에는 건의", "feedback");

-- -- 게시글 이미지 더미 데이터
-- INSERT INTO board_images(board_id, original_filename, stored_filename)
-- VALUES (1, "sample1.jpg", "sample1.jpg");

-- -- 게시글 댓글 더미 데이터
-- INSERT INTO board_comments(user_id, board_id, content)
-- VALUES (1, 1, "댓글 작성합니다."),
--        (1, 1, "두 번째 댓글입니다.");

-- INSERT INTO track_comments(user_id, track_id, content)
-- VALUES (1, 1, "이 코스 적극 추천합니다!!"),
--        (2, 1, "저한테는 이 코스가 조금 힘들었어요. 그리고 벌레 조심하세요.");

-- INSERT INTO track_comment_images(comment_id, original_filename, stored_filename)
-- VALUES (1, "원본1", "저장1"),
--        (1, "원본2", "저장2")