DROP TABLE IF EXISTS board_images;
DROP TABLE IF EXISTS board_comments;
DROP TABLE IF EXISTS track_comment_images;
DROP TABLE IF EXISTS track_comments;
-- track, user에 의존하는 테이블
DROP TABLE IF EXISTS boards;
DROP TABLE IF EXISTS favorites;
DROP TABLE IF EXISTS recommends;
DROP TABLE IF EXISTS tracks;
-- 외래키가 없는 테이블
DROP TABLE IF EXISTS mountains;
DROP TABLE IF EXISTS users;
-- fix

CREATE TABLE IF NOT EXISTS users (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    pass VARCHAR(255) NOT NULL,
    name VARCHAR(50) NOT NULL,
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS mountains (
    id BIGINT AUTO_INCREMENT PRIMARY KEY ,
    name VARCHAR(50) NOT NULL,
    location VARCHAR(255) NOT NULL ,
    height BIGINT NOT NULL ,
    description TEXT NOT NULL,
    original_filename varchar(255),
    stored_filename varchar(255)
);

CREATE TABLE IF NOT EXISTS tracks (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mountain_id BIGINT NOT NULL ,
    name VARCHAR(255) NOT NULL ,
    gpx_file_path varchar(255) NOT NULL ,
    foreign key (mountain_id) REFERENCES mountains(id) on delete cascade
);

CREATE TABLE IF NOT EXISTS recommends (
    user_id BIGINT,
    track_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (user_id, track_id),
    foreign key (user_id) REFERENCES users(id) on delete cascade ,
    foreign key (track_id) REFERENCES tracks(id) on delete cascade
);

CREATE TABLE IF NOT EXISTS favorites (
    user_id BIGINT,
    track_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (user_id, track_id),
    foreign key (user_id) REFERENCES users(id) on delete cascade ,
    foreign key (track_id) REFERENCES tracks(id) on delete cascade
);

CREATE TABLE IF NOT EXISTS boards (
			id BIGINT AUTO_INCREMENT PRIMARY KEY,
       user_id BIGINT NOT NULL,
      track_id BIGINT DEFAULT NULL,
		 title VARCHAR(50) NOT NULL,
       content TEXT NOT NULL,
      category ENUM('free', 'feedback') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT NULL,
    deleted_at TIMESTAMP DEFAULT NULL,
	
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (track_id) REFERENCES tracks(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS board_images (
                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
             board_id BIGINT NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
      stored_filename VARCHAR(255) NOT NULL,

    FOREIGN KEY (board_id) REFERENCES boards(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS board_comments (
            id BIGINT AUTO_INCREMENT PRIMARY KEY,
       user_id BIGINT NOT NULL,
      board_id BIGINT NOT NULL,
       content VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT NULL,
    deleted_at TIMESTAMP DEFAULT NULL,

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (board_id) REFERENCES boards(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS track_comments (
            id BIGINT AUTO_INCREMENT PRIMARY KEY,
       user_id BIGINT NOT NULL,
      track_id BIGINT DEFAULT NULL,
       content VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT NULL,
    deleted_at TIMESTAMP DEFAULT NULL,

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (track_id) REFERENCES tracks(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS track_comment_images (
                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
           comment_id BIGINT NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
      stored_filename VARCHAR(255) NOT NULL,

    FOREIGN KEY (comment_id) REFERENCES track_comments(id) ON DELETE CASCADE
);