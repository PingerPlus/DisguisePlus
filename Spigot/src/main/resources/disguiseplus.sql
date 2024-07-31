CREATE TABLE IF NOT EXISTS users(
    uuid VARCHAR(36) PRIMARY KEY NOT NULL,
    username VARCHAR(16) NOT NULL,
    join_date DATETIME NOT NULL,
    last_login DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS skins(
    skin_id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    skin_value VARCHAR(1000) UNIQUE NOT NULL,
    skin_signature VARCHAR(1000) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS disguises(
    uuid VARCHAR(36) NOT NULL,
    skin_id INT,
    name VARCHAR(16) NOT NULL,
    `rank` VARCHAR(100),
    start_time DATETIME NOT NULL,
    end_time DATETIME,
    PRIMARY KEY (uuid, start_time),
    FOREIGN KEY (uuid) REFERENCES users(uuid),
    FOREIGN KEY (skin_id) REFERENCES skins(skin_id)
);
