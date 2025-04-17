-- 创建文章表
CREATE TABLE IF NOT EXISTS posts (
    post_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    category_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    cover_image VARCHAR(255),
    status ENUM('draft', 'published') DEFAULT 'draft',
    view_count INT DEFAULT 0,
    like_count INT DEFAULT 0,
    create_at DATETIME NOT NULL,
    update_at DATETIME NOT NULL,
    publish_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(userid),
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

-- 创建分类表
CREATE TABLE IF NOT EXISTS categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    post_count INT DEFAULT 0,
    create_at DATETIME NOT NULL,
    update_at DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(userid)
); 