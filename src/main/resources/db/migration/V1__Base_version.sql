CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    token VARCHAR(255) DEFAULT NULL,
    token_valid_date TIMESTAMP DEFAULT NULL,
    role VARCHAR(255) NOT NULL
);
