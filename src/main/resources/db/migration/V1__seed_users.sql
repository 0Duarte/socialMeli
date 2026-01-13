-- Tabela users
CREATE TABLE users (
                       id INT NOT NULL AUTO_INCREMENT,
                       user_name VARCHAR(15) NOT NULL UNIQUE,
                       is_seller TINYINT(1) NOT NULL,
                       PRIMARY KEY (id)
);

-- Tabela products
CREATE TABLE products (
                          id INT NOT NULL,
                          name  VARCHAR(40) NOT NULL,
                          type  VARCHAR(15) NOT NULL,
                          brand VARCHAR(25) NOT NULL,
                          color VARCHAR(15) NOT NULL,
                          notes VARCHAR(255),
                          PRIMARY KEY (id)
);

-- Tabela posts
CREATE TABLE posts (
                       id INT NOT NULL AUTO_INCREMENT,
                       user_id    INT NOT NULL,
                       date       DATE,
                       product_id INT NOT NULL,
                       category   INT,
                       price      DOUBLE,
                       has_promo  TINYINT(1),
                       discount   DOUBLE,
                       PRIMARY KEY (id),
                       CONSTRAINT fk_posts_user
                           FOREIGN KEY (user_id) REFERENCES users(id),
                       CONSTRAINT fk_posts_product
                           FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Tabela user_follows (ManyToMany User <-> User)
CREATE TABLE user_follows (
                              follower_id INT NOT NULL,
                              seller_id   INT NOT NULL,
                              PRIMARY KEY (follower_id, seller_id),
                              CONSTRAINT fk_user_follows_follower
                                  FOREIGN KEY (follower_id) REFERENCES users(id),
                              CONSTRAINT fk_user_follows_seller
                                  FOREIGN KEY (seller_id) REFERENCES users(id)
);

-- SEED USERS
INSERT INTO users (id, user_name, is_seller) VALUES (1, 'Arildo',  TRUE);
INSERT INTO users (id, user_name, is_seller) VALUES (2, 'Bianca',  FALSE);
INSERT INTO users (id, user_name, is_seller) VALUES (3, 'Carlinhos', FALSE);
INSERT INTO users (id, user_name, is_seller) VALUES (4, 'Diego',   FALSE);
INSERT INTO users (id, user_name, is_seller) VALUES (5, 'Eduardo', FALSE);
INSERT INTO users (id, user_name, is_seller) VALUES (6, 'Felipe',  FALSE);

-- SEED FOLLOWS
INSERT INTO user_follows (follower_id, seller_id) VALUES (2, 1);
INSERT INTO user_follows (follower_id, seller_id) VALUES (3, 1);
INSERT INTO user_follows (follower_id, seller_id) VALUES (4, 1);
INSERT INTO user_follows (follower_id, seller_id) VALUES (5, 1);
INSERT INTO user_follows (follower_id, seller_id) VALUES (6, 1);

-- SEED PRODUCTS
INSERT INTO products (id, brand,  color,  name,                notes,                                                                                           type)
VALUES               (1, 'Sansung', 'Preto', 'Sansung Galaxy S22', 'Tela: 6.1 polegadas, Processador: Exynos 2200, Câmera: Tripla 50MP + 10MP + 12MP, Bateria: 3700mAh', 'SMARTPHONE');

INSERT INTO products (id, brand,  color,  name,      notes,                                                                                       type)
VALUES               (2, 'Apple', 'Branco', 'iPhone 13', 'Tela: 6.1 polegadas, Processador: A15 Bionic, Câmera: Dupla 12MP + 12MP, Bateria: 3240mAh', 'SMARTPHONE');

INSERT INTO products (id, brand,   color,  name,          notes,                                                                                           type)
VALUES               (3, 'Xiaomi', 'Cinza', 'Xiaomi Mi 11', 'Tela: 6.81 polegadas, Processador: Snapdragon 888, Câmera: Tripla 108MP + 13MP + 5MP, Bateria: 4600mAh', 'SMARTPHONE');

-- SEED POSTS
INSERT INTO posts(category, date,        has_promo, price, product_id, user_id)
VALUES          (1,        '2026-01-13', FALSE,     1000,  1,          1);

INSERT INTO posts(category, date,        has_promo, price, product_id, user_id)
VALUES          (1,        '2026-01-14', FALSE,     1500,  2,          1);

INSERT INTO posts(category, date,        has_promo, price, product_id, user_id)
VALUES          (1,        '2026-01-15', FALSE,     2000,  3,          1);