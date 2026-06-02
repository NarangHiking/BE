DROP TABLE IF EXISTS board_images;
DROP TABLE IF EXISTS board_comments;
DROP TABLE IF EXISTS boards;
DROP TABLE IF EXISTS tracks;
DROP TABLE IF EXISTS users;


CREATE TABLE IF NOT EXISTS users (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    pass VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL,
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS mountains (
    id BIGINT AUTO_INCREMENT PRIMARY KEY ,
    name VARCHAR(50) NOT NULL,
    height BIGINT NOT NULL ,
    description TEXT NOT NULL ,
    original_filename VARCHAR(255),
    stored_filename VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS tracks (
	id BIGINT AUTO_INCREMENT PRIMARY KEY
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
	
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (track_id) REFERENCES tracks(id)
);

CREATE TABLE IF NOT EXISTS board_images (
                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
             board_id BIGINT NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
      stored_filename VARCHAR(255) NOT NULL,

    FOREIGN KEY (board_id) REFERENCES boards(id)
);

CREATE TABLE IF NOT EXISTS board_comments (
            id BIGINT AUTO_INCREMENT PRIMARY KEY,
       user_id BIGINT NOT NULL,
      board_id BIGINT NOT NULL,
       content VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT NULL,
    deleted_at TIMESTAMP DEFAULT NULL,

    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (board_id) REFERENCES boards(id)
);