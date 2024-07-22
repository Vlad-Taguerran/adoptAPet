CREATE TABLE tb_pet
(
    id          BINARY(16) NOT NULL,
    name        VARCHAR(80) NOT NULL ,
    description TEXT NOT NULL,
    category    VARCHAR(100) NOT NULL,
    status      VARCHAR(100) DEFAULT 'Disponível',
    born_in     DATE ,
    url_image   VARCHAR(255) NULL,
    active      TINYINT(1) DEFAULT 1,
    PRIMARY KEY (id)
)