# SocialMeli API

![Java](https://img.shields.io/badge/Java-21-007396?logo=java&logoColor=white&style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.0-6DB33F?logo=springboot&logoColor=white&style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven-3.9.x-C71A36?logo=apachemaven&logoColor=white&style=for-the-badge)
![JUnit](https://img.shields.io/badge/JUnit-5.10.x-25A162?logo=junit5&logoColor=white&style=for-the-badge)
![MySQL](https://img.shields.io/badge/MySQL-8.x-4479A1?logo=mysql&logoColor=white&style=for-the-badge)
![H2](https://img.shields.io/badge/H2-2.x-7CBF00?logo=h2&logoColor=white&style=for-the-badge)
![Flyway](https://img.shields.io/badge/Flyway-10.10.x-CC0200?logo=flyway&logoColor=white&style=for-the-badge)
![Mockito](https://img.shields.io/badge/Mockito-5.x-4CAF50?logo=mockito&logoColor=white&style=for-the-badge)
![OpenAPI](https://img.shields.io/badge/OpenAPI-2.x-6BA539?logo=openapiinitiative&logoColor=white&style=for-the-badge)

[![Build](https://img.shields.io/badge/Build-Maven-orange.svg)](https://maven.apache.org/)
[![Tests](https://img.shields.io/badge/Tests-JUnit%205-%23bb3f3f.svg)](https://junit.org/junit5/)
[![Database](https://img.shields.io/badge/DB-MySQL%20%7C%20H2-lightgrey.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/license-MIT-informational.svg)](LICENSE)

API REST desenvolvida como solução do desafio **SocialMeli** do bootcamp Mercado Livre.  
Simula funcionalidades sociais: seguir vendedores, listar seguidores/seguidos, registrar publicações (incluindo promoções) e expor um feed de posts.

---

## Tecnologias

- Java 21
- Spring Boot 3.3.0
- Spring Web, Spring Data JPA, Validation
- MySQL (runtime), H2 (testes)
- JUnit, Spring Boot Test, MockMvc
- Flyway
- Mockito
- Swagger / OpenAPI
- Maven

---

## Estrutura de Pacotes

- `meli.socialmeli.controller` – controllers REST
- `meli.socialmeli.services` – regras de negócio
- `meli.socialmeli.model` – entidades JPA
- `meli.socialmeli.dto.request` – DTOs de entrada
- `meli.socialmeli.dto.response` – DTOs de saída
- `meli.socialmeli.repository` – repositórios JPA
- `meli.socialmeli.integration` – testes de integração
- `meli.socialmeli.unit` – testes unitários 

---

## Funcionalidades (User Stories)

- **US0001** – Seguir um vendedor
- **US0002** – Obter número de seguidores de um vendedor
- **US0003** – Listar seguidores de um vendedor (com ordenação opcional por nome)
- **US0004** – Listar vendedores seguidos por um usuário (com ordenação opcional por nome)
- **US0005** – Registrar uma nova publicação
- **US0006** – Obter posts das últimas 2 semanas de vendedores seguidos
- **US0007** – Deixar de seguir um vendedor
- **US0008** – Ordenação alfabética asc/desc (aplicada às listas de seguidores/seguidos)
- **US0009** – Ordenação por data asc/desc (aplicada ao feed de posts)
- **US0010** – Registrar nova publicação promocional
- **US0011** – Obter contagem de publicações promocionais de um vendedor

---

## Testes de Integração

Local: `meli.socialmeli.integration.FollowIntegrationTest`

- `@SpringBootTest`, `@AutoConfigureMockMvc`, `@ActiveProfiles("test")`
- Banco H2 em memória, limpo a cada teste
- Testes cobrem ponta a ponta:
    - follow/unfollow e relacionamentos `followers`/`followings`
    - contagem e listagem de seguidores/seguidos com ordenação
    - criação de posts e promo posts
    - feed de posts (filtro por seguidores + janela de 2 semanas + ordenação por data)
    - contagem de posts promocionais

---

## Testes Unitários

Os testes unitários estão localizados em `src/test/java/meli/socialmeli/unit` e cobrem as regras de negócio dos serviços (`FollowService`, `PostService`, etc).

- Utiliza JUnit 5 (`@Test`, `@BeforeEach`, etc)
- Mockito para mocks de dependências
- Spring Boot Test para contexto e injeção
- Banco H2 em memória para isolamento dos testes

Execute todos os testes unitários com:

```bash
mvn test
```
---

## Execução

### Requisitos

- Java 17+
- Maven
- Docker (opcional, para rodar o MySQL)
- MySQL rodando (para o profile padrão)

### Rodar a aplicação

```bash
mvn spring-boot:run
```
