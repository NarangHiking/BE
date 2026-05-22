
INSERT INTO users(id) VALUES (DEFAULT);
INSERT INTO tracks(id) VALUES (DEFAULT);

-- boadrs 테이블 더미데이터
INSERT INTO boards(user_id, title, content, category)
VALUES (1, "등산", "테스트", "free");

INSERT INTO boards(user_id, track_id, title, content, category)
VALUES (2, 3, "게시글", "이번에는 건의", "feedback");

select * from boards;