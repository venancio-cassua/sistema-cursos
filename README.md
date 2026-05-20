# Sistema de Cursos Online

API REST para gerenciamento de cursos online, com autenticação de usuários administradores, cadastro de cursos, cadastro de alunos e gerenciamento de matrículas.

## Funcionalidades

- Autenticação de usuários
- Gerenciamento de cursos
- Gerenciamento de alunos
- Gerenciamento de matrículas
- Segurança com Spring Security
- Persistência com Spring Data JPA

## Tecnologias

- Java 17
- Spring Boot 4.0.6
- Spring Web
- Spring Data JPA
- Spring Security
- Spring Validation
- MariaDB

## Entidades

### Usuário
Responsável pela autenticação.

| Campo | Tipo |
|---|---|
| id | Long |
| nome | String |
| login | String |
| senha | String |

### Curso

| Campo | Tipo |
|---|---|
| id | Long |
| nome | String |
| descricao | String |
| cargaHoraria | Integer |

### Aluno

| Campo | Tipo |
|---|---|
| id | Long |
| nome | String |
| email | String |

### Relacionamento

`Aluno` e `Curso` possuem relacionamento N-N — um aluno pode estar em vários cursos e um curso pode ter vários alunos. Isso gera a tabela intermediária `aluno_curso`.

## Estrutura do Projeto

```
src/main/java/com/furb/sistemacursos
├── controller
├── service
├── repository
├── models
├── dtos
├── security
└── exception
```

## Como Rodar

### Pré-requisitos

- Docker e Docker Compose instalados
- Maven (ou usar o `mvnw` incluso no projeto)

### 1. Configurar variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto (use o `.env.example` como base):

```env
DB_ROOT_PASSWORD=sua_senha
DB_NAME=cursos
DB_USERNAME=root
```

### 2. Gerar o JAR

```bash
./mvnw package -DskipTests
```

### 3. Subir os containers

```bash
docker compose up --build
```

A API estará disponível em `http://localhost:8080`.

### Rodar localmente (sem Docker)

Certifique-se de ter um MariaDB rodando na porta 3306 com o banco `cursos` criado, então:

```bash
./mvnw spring-boot:run
```

## Variáveis de Ambiente

| Variável | Descrição | Padrão |
|---|---|---|
| `SPRING_DATASOURCE_URL` | URL de conexão com o banco | `jdbc:mariadb://localhost:3306/cursos` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco | `root` |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco | — |
