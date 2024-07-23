# AdoptaPet - Aplicação Spring Boot com Docker Compose

Este repositório contém uma aplicação Spring Boot configurada para rodar com Docker Compose. A aplicação consiste de um backend em Spring Boot e um banco de dados MySQL.

## Pré-requisitos

Certifique-se de ter as seguintes ferramentas instaladas em sua máquina:

- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/)

## Estrutura do Projeto

- `Dockerfile`: Arquivo de configuração para construir a imagem Docker do backend Spring Boot.
- `docker-compose.yml`: Arquivo de configuração do Docker Compose para orquestrar a aplicação e o banco de dados.
- `src/main/resources/application.properties`: Arquivo de configuração do Spring Boot.
- `src/main/resources/db-init`: Diretório contendo scripts SQL para inicializar o banco de dados.

## Configuração do Docker Compose

O arquivo `docker-compose.yml` está configurado da seguinte maneira:

```yaml
version: '3.3'

services:

  app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: 'adoptapet-back'
    ports:
      - "8081:8081"
    volumes:
      - ./build/libs:/libs
      - ./src/main/resources/static/images:/app/src/main/resources/static/images
    restart: always
    depends_on:
      - db
    networks:
      - db
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/adoptapet?createDatabaseIfNoExist=true
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: 1234
      SPRING.FLYWAY.URL: jdbc:mysql://db:3306/adoptapet?createDatabaseIfNoExist=true
      SPRING.FLYWAY.USER: root
      SPRING.FLYWAY.PASSWORD: 1234
    entrypoint: [ "java", "-jar", "/libs/adotapet-0.0.1-SNAPSHOT.jar" ]

  db:
    image: 'mysql'
    container_name: adoptapet-db
    environment:
      MYSQL_ROOT_PASSWORD: '1234'
      MYSQL_DATABASE: 'adoptapet'
    ports:
      - "4022:3306"
    volumes:
      - mysql-data:/var/lib/mysql
      - ./db-init:/docker-entrypoint-initdb.d

volumes:
  mysql-data:

networks:
  db:
    driver: bridge



## Rodando a aplicação
git clone https://github.com/Vlad-Taguerran/adoptAPet.git
cd adoptapet

docker-compose up --build
a aplicação estará rodando na porta 8081 do localhost