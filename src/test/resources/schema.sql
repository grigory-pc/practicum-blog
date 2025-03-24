CREATE TABLE IF NOT EXISTS posts (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     title VARCHAR(255) NOT NULL,
    image_path VARCHAR(255),
    text VARCHAR NOT NULL,
    count_likes INT NOT NULL
    );

CREATE TABLE IF NOT EXISTS tags (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    tag_name VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS posts_tags (
                                          post_id BIGINT,
                                          tag_id BIGINT,
                                          PRIMARY KEY (post_id, tag_id)
    );

CREATE TABLE IF NOT EXISTS comments (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        post_id BIGINT NOT NULL,
                                        text VARCHAR NOT NULL
);

ALTER TABLE posts_tags
    ADD FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE;

ALTER TABLE posts_tags
    ADD FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE;

ALTER TABLE comments
    ADD FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE;