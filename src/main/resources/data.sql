
-- users 테이블 더미데이터
INSERT INTO users (email, pass, name, role) VALUES
    ('admin@naranghiking.com', '$2a$10$c250VxjvhHZeC/KH6U.DuepbKEoK6zV6assPlL84ngrLkvKfuOc.q', '관리자', 'ADMIN'),
    ('hong@test.com', '$2a$10$BuGl5LRUoahZLlUlh2oWR.mcrDmEoTdXSi1bDp3VDL5z5EqY1VKmK', '홍길동', 'USER'),
    ('kim@test.com', '$2a$10$BuGl5LRUoahZLlUlh2oWR.mcrDmEoTdXSi1bDp3VDL5z5EqY1VKmK', '김철수', 'USER'),
    ('lee@test.com', '$2a$10$BuGl5LRUoahZLlUlh2oWR.mcrDmEoTdXSi1bDp3VDL5z5EqY1VKmK', '이영희', 'USER'),
    ('park@test.com', '$2a$10$BuGl5LRUoahZLlUlh2oWR.mcrDmEoTdXSi1bDp3VDL5z5EqY1VKmK', '박민수', 'USER'),
    ('choi@test.com', '$2a$10$BuGl5LRUoahZLlUlh2oWR.mcrDmEoTdXSi1bDp3VDL5z5EqY1VKmK', '최지우', 'USER'),
    ('jung@test.com', '$2a$10$BuGl5LRUoahZLlUlh2oWR.mcrDmEoTdXSi1bDp3VDL5z5EqY1VKmK', '정수진', 'USER'),
    ('kang@test.com', '$2a$10$BuGl5LRUoahZLlUlh2oWR.mcrDmEoTdXSi1bDp3VDL5z5EqY1VKmK', '강현우', 'USER'),
    ('yoon@test.com', '$2a$10$BuGl5LRUoahZLlUlh2oWR.mcrDmEoTdXSi1bDp3VDL5z5EqY1VKmK', '서연', 'USER'),
    ('oh@test.com', '$2a$10$BuGl5LRUoahZLlUlh2oWR.mcrDmEoTdXSi1bDp3VDL5z5EqY1VKmK', '오재훈', 'ADMIN');

INSERT INTO users (email, pass, name, role, delete_at) VALUES
    ('deleted@test.com', '$2a$10$BuGl5LRUoahZLlUlh2oWR.mcrDmEoTdXSi1bDp3VDL5z5EqY1VKmK', '탈퇴유저', 'USER', '2026-04-01 10:00:00');

-- 트랙 더미 데이터
INSERT INTO tracks (id) VALUES
      (1),
      (2),
      (3);


-- boadrs 테이블 더미데이터
INSERT INTO boards(user_id, title, content, category)
VALUES (1, "등산", "테스트", "free");

INSERT INTO boards(user_id, track_id, title, content, category)
VALUES (2, 3, "게시글", "이번에는 건의", "feedback");

select * from boards;

-- 게시글 이미지 더미 데이터
INSERT INTO board_images(board_id, original_filename, stored_filename)
VALUES (1, "sample1.jpg", "sample1.jpg");

-- 게시글 댓글 더미 데이터
INSERT INTO board_comments(user_id, board_id, content)
VALUES (1, 1, "댓글 작성합니다."),
       (1, 1, "두 번째 댓글입니다.");