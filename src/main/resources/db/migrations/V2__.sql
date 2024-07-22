ALTER TABLE tb_pet
    DROP COLUMN born_in;

ALTER TABLE tb_pet
    ADD born_in datetime NULL;

ALTER TABLE tb_pet
    MODIFY category VARCHAR(255);

ALTER TABLE tb_pet
    MODIFY category VARCHAR(255) NULL;

ALTER TABLE tb_pet
    MODIFY `description` LONGTEXT NULL;

ALTER TABLE tb_pet
    MODIFY status ENUM('Disponível','Adotado') default  'Disponível';