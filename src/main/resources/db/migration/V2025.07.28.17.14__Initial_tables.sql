CREATE TABLE brand (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255),
    logo_url VARCHAR(255) NOT NULL
);

CREATE TABLE series (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE publisher (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255),
    logo_url VARCHAR(255) NOT NULL
);

CREATE TABLE base_collectable (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    image_url VARCHAR(255) NOT NULL,
    initial_price VARCHAR(255) NOT NULL,
    market_price VARCHAR(255) NOT NULL,
    quantity INT NOT NULL
);

CREATE TABLE art_book (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    bc_id BIGINT NOT NULL,
    brand_id BIGINT NOT NULL,
    publisher_id BIGINT NOT NULL,
    isbn VARCHAR(255),
    FOREIGN KEY (bc_id) REFERENCES base_collectable (id),
    FOREIGN KEY (brand_id) REFERENCES brand (id),
    FOREIGN KEY (publisher_id) REFERENCES publisher (id)
);

CREATE TABLE collectable_type (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE collectable (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    bc_id BIGINT NOT NULL,
    collectable_type_id BIGINT NOT NULL,
    brand_id BIGINT NOT NULL,
    series_id BIGINT NOT NULL,
    sku_id VARCHAR(255) NOT NULL,
    FOREIGN KEY (bc_id) REFERENCES base_collectable (id),
    FOREIGN KEY (collectable_type_id) REFERENCES collectable_type (id),
    FOREIGN KEY (brand_id) REFERENCES brand (id),
    FOREIGN KEY (series_id) REFERENCES series (id)
);

CREATE TABLE card_rarity (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    brand_id BIGINT NOT NULL,
    FOREIGN KEY (brand_id) REFERENCES brand (id)
);

CREATE TABLE card_set (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    brand_id BIGINT NOT NULL,
    series_id BIGINT,
    FOREIGN KEY (brand_id) REFERENCES brand (id),
    FOREIGN KEY (series_id) REFERENCES series (id)
);

CREATE TABLE card (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    bc_id BIGINT NOT NULL,
    brand_id BIGINT NOT NULL,
    series_id BIGINT,
    card_set_id BIGINT NOT NULL,
    card_rarity_id BIGINT NOT NULL,
    card_number INT NOT NULL,
    FOREIGN KEY (bc_id) REFERENCES base_collectable (id),
    FOREIGN KEY (brand_id) REFERENCES brand (id),
    FOREIGN KEY (series_id) REFERENCES series (id),
    FOREIGN KEY (card_set_id) REFERENCES card_set (id),
    FOREIGN KEY (card_rarity_id) REFERENCES card_rarity (id)
);

CREATE TABLE card_product_type (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE sealed_card_product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    bc_id BIGINT NOT NULL,
    cpt_id BIGINT NOT NULL,
    card_set_id BIGINT NOT NULL,
    brand_id BIGINT NOT NULL,
    series_id BIGINT NOT NULL,
    sku_id VARCHAR(255) NOT NULL,
    FOREIGN KEY (bc_id) REFERENCES base_collectable (id),
    FOREIGN KEY (cpt_id) REFERENCES card_product_type (id),
    FOREIGN KEY (card_set_id) REFERENCES card_set (id),
    FOREIGN KEY (brand_id) REFERENCES brand (id),
    FOREIGN KEY (series_id) REFERENCES series (id)
);

CREATE TABLE shoe_model (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    brand_id BIGINT NOT NULL,
    FOREIGN KEY (brand_id) REFERENCES brand (id)
);

CREATE TABLE shoe (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    bc_id BIGINT NOT NULL,
    shoe_model_id BIGINT NOT NULL,
    brand_id BIGINT NOT NULL,
    size VARCHAR(50) NOT NULL,
    color VARCHAR(255) NOT NULL,
    sku_id VARCHAR(255) NOT NULL,
    FOREIGN KEY (bc_id) REFERENCES base_collectable (id),
    FOREIGN KEY (shoe_model_id) REFERENCES shoe_model (id),
    FOREIGN KEY (brand_id) REFERENCES brand (id)
);

CREATE TABLE console (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    brand_id BIGINT NOT NULL,
    FOREIGN KEY (brand_id) REFERENCES brand (id)
);

CREATE TABLE video_game (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    bc_id BIGINT NOT NULL,
    console_id BIGINT NOT NULL,
    brand_id BIGINT NOT NULL,
    series_id BIGINT NOT NULL,
    publisher_id BIGINT NOT NULL,
    sku_id VARCHAR(255) NOT NULL,
    FOREIGN KEY (bc_id) REFERENCES base_collectable (id),
    FOREIGN KEY (console_id) REFERENCES console (id),
    FOREIGN KEY (brand_id) REFERENCES brand (id),
    FOREIGN KEY (series_id) REFERENCES series (id),
    FOREIGN KEY (publisher_id) REFERENCES publisher (id)
);
