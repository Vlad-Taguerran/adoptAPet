-- Crie o usuário 'adopt' com a senha '1234'
CREATE USER 'adopt'@'%' IDENTIFIED BY '1234';

-- Conceda todas as permissões ao usuário 'adopt' no banco de dados 'adoptapet'
GRANT ALL PRIVILEGES ON adoptapet.* TO 'adopt'@'%';

-- Atualize as permissões
FLUSH PRIVILEGES;