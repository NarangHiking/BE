
-- users 테이블 더미데이터
INSERT INTO users (email, pass, name, role) VALUES
    ('admin@naranghiking.com', '{noop}admin1234', '관리자', 'ADMIN'),
    ('hong@test.com', '{noop}pass1234', '홍길동', 'USER'),
    ('kim@test.com', '{noop}pass1234', '김철수', 'USER'),
    ('lee@test.com', '{noop}pass1234', '이영희', 'USER'),
    ('park@test.com', '{noop}pass1234', '박민수', 'USER'),
    ('choi@test.com', '{noop}pass1234', '최지우', 'USER'),
    ('jung@test.com', '{noop}pass1234', '정수진', 'USER'),
    ('kang@test.com', '{noop}pass1234', '강현우', 'USER'),
    ('yoon@test.com', '{noop}pass1234', '윤서연', 'USER'),
    ('oh@test.com', '{noop}pass1234', '오재훈', 'ADMIN');

-- 소프트 삭제 예시 (탈퇴한 유저)
INSERT INTO users (email, pass, name, role, delete_at) VALUES
    ('deleted@test.com', '{noop}pass1234', '탈퇴유저', 'USER', '2026-04-01 10:00:00');

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