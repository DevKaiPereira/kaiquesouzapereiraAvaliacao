<div align="center">

# 📄 VEXYN — Sistema de Gerenciamento de Documentos

**Uma API REST robusta para upload, listagem, atualização e controle de status de documentos,**  
**protegida por JWT e API Key, construída com Spring Boot 3 e Java 21.**

[![Java](https://img.shields.io/badge/Java-21-blue?logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.11-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8-blue?logo=mysql)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue?logo=docker)](https://www.docker.com/)
[![Swagger](https://img.shields.io/badge/Swagger-OpenAPI%203-green?logo=swagger)](https://swagger.io/)

</div>

---

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Stack Tecnológico](#-stack-tecnológico)
- [Arquitetura do Projeto](#-arquitetura-do-projeto)
- [Módulos e Componentes](#-módulos-e-componentes)
- [Segurança e Autenticação](#-segurança-e-autenticação)
- [Logging e Monitoramento](#-logging-e-monitoramento)
- [Documentação dos Endpoints](#-documentação-dos-endpoints)
- [Como Executar o Projeto](#-como-executar-o-projeto)
- [Variáveis de Ambiente](#-variáveis-de-ambiente)
- [Testes](#-testes)
- [Roadmap](#-roadmap)

---

## 🚀 Sobre o Projeto

O **VEXYN** é um sistema de gerenciamento de documentos que permite:

- 📤 **Upload** de arquivos com armazenamento de metadados no banco de dados MySQL
- 📋 **Listagem** de todos os documentos cadastrados
- ♻️ **Atualização** de arquivos existentes
- 🗑️ **Exclusão lógica** (soft delete via mudança de status)
- 🔄 **Controle de status** dos documentos (`PROCESSING`, `READY`, `UPDATED`, `DELETED`)
- 🔐 **Autenticação dupla**: JWT Bearer Token + API Key/Secret
- 🛡️ **Controle de acesso por perfis**: `ADMIN`, `MANAGER` e `USER`
- 📊 **Relatório diário automático** com total de arquivos e espaço utilizado

---

## 🛠️ Stack Tecnológico

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 21 | Linguagem principal |
| Spring Boot | 3.5.11 | Framework principal |
| Spring Security | (via Boot) | Autenticação e autorização |
| Spring Data JPA | (via Boot) | Persistência de dados |
| Spring Validation | (via Boot) | Validação de DTOs |
| MySQL | 8+ | Banco de dados relacional |
| Hibernate | (via Boot) | ORM |
| JWT (Auth0) | 4.4.0 | Geração e validação de tokens |
| Log4j2 | (via Boot) | Sistema de logging assíncrono |
| LMAX Disruptor | 3.4.4 | Log4j2 assíncrono de alta performance |
| SpringDoc OpenAPI | 2.8.15 | Documentação Swagger/OpenAPI 3 |
| Lombok | (via Boot) | Redução de boilerplate |
| Docker Compose | — | Provisionamento do banco de dados |

---

## 🏗️ Arquitetura do Projeto

O projeto segue o padrão **MVC em camadas**, organizado por módulos funcionais:

```
src/main/java/com/kaiquesouzapereira/javaavaliacao/
│
├── JavaavaliacaoApplication.java          # Classe principal (entry point)
│
└── modules/
    ├── documents/                         # 📄 Módulo de Documentos
    │   ├── Controller/
    │   │   └── DocumentController.java    # Endpoints REST de documentos
    │   ├── Service/
    │   │   ├── UploadDocumentService.java
    │   │   ├── FetchAllDocumentService.java
    │   │   ├── UpdateDocumentService.java
    │   │   ├── DeleteDocumentService.java
    │   │   ├── ChangeStatusDocumentService.java
    │   │   └── DocumentStatsService.java  # Relatório diário agendado
    │   ├── Entity/
    │   │   └── DocumentEntity.java        # Entidade JPA + enum Status
    │   ├── Repository/
    │   │   └── DocumentRepository.java    # Spring Data JPA
    │   ├── Dto/
    │   │   ├── DocumentResponseDto.java
    │   │   └── ChangeStatusDto.java
    │   └── configOpenAiDoc/
    │       └── OpenApiConfig.java         # Configuração Swagger/OpenAPI
    │
    ├── users/                             # 👤 Módulo de Usuários
    │   ├── Controller/
    │   │   ├── UserController.java        # Criação de usuário
    │   │   └── AuthUserController.java    # Autenticação e geração de JWT
    │   ├── Service/
    │   │   ├── CreateUserService.java
    │   │   └── AuthenticateUserService.java
    │   ├── Entity/
    │   │   └── UserEntity.java            # Entidade JPA + enum Position
    │   ├── Repository/
    │   │   └── UserRepository.java
    │   └── Dto/
    │       ├── CreateUserDto.java
    │       └── AuthUserDto.java
    │
    ├── security/                          # 🔐 Segurança
    │   ├── SecurityConfig.java            # Configuração do Spring Security
    │   ├── SecurityFilter.java            # Filtro JWT
    │   └── ApiKeyFilter.java              # Filtro de API Key + Secret
    │
    ├── providers/
    │   └── JwtProvider.java               # Verificação de tokens JWT
    │
    └── exceptions/                        # ⚠️ Exceções customizadas
        ├── DocumentNotFound.java
        ├── UserAlreadyExists.java
        └── InvalidCredentials.java
```

### Fluxo de uma requisição

```
HTTP Request
    │
    ▼
ApiKeyFilter          ← valida X-Api-Key + X-Api-Secret (rotas de documentos)
    │
    ▼
SecurityFilter        ← valida Bearer JWT (rotas autenticadas)
    │
    ▼
Controller            ← recebe a requisição, chama o Service
    │
    ▼
Service               ← contém a lógica de negócio
    │
    ▼
Repository (JPA)      ← persiste ou consulta dados no MySQL
    │
    ▼
HTTP Response
```

---

## 🧩 Módulos e Componentes

### 👤 Módulo de Usuários

#### Entidade (`UserEntity`)

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | `UUID` | Identificador único (gerado automaticamente) |
| `username` | `String` | Nome do usuário |
| `email` | `String` | E-mail único do usuário |
| `password` | `String` | Senha criptografada (BCrypt) |
| `position` | `Position` | Perfil: `ADMIN`, `MANAGER` ou `USER` |
| `createdAt` | `LocalDateTime` | Data de criação (automática) |

#### Serviços

- **`CreateUserService`** — Cria um novo usuário, criptografando a senha com BCrypt. Lança `UserAlreadyExists` se o e-mail já estiver cadastrado.
- **`AuthenticateUserService`** — Valida credenciais (e-mail + senha) e gera um JWT com validade de 7 dias, contendo o `id`, `name` e `roles` do usuário.

---

### 📄 Módulo de Documentos

#### Entidade (`DocumentEntity`)

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | `UUID` | Identificador único (gerado automaticamente) |
| `name` | `String` | Nome original do arquivo |
| `type` | `String` | Tipo MIME do arquivo |
| `data` | `byte[]` | Conteúdo binário do arquivo (`LONGBLOB`) |
| `path` | `String` | Caminho físico no servidor |
| `status` | `Status` | Status atual do documento |
| `createdAt` | `LocalDateTime` | Data de criação (automática) |
| `updatedAt` | `LocalDateTime` | Data da última atualização (automática) |

#### Status disponíveis

| Status | Descrição |
|---|---|
| `PROCESSING` | Status inicial após o upload |
| `READY` | Documento aprovado e pronto para uso |
| `UPDATED` | Arquivo foi substituído por uma nova versão |
| `DELETED` | Documento excluído logicamente (soft delete) |

#### Serviços

| Serviço | Descrição |
|---|---|
| `UploadDocumentService` | Salva o arquivo no disco e persiste os metadados no banco |
| `FetchAllDocumentService` | Retorna todos os documentos exceto os com status `DELETED` |
| `UpdateDocumentService` | Substitui o conteúdo do arquivo se for diferente do atual |
| `DeleteDocumentService` | Marca o documento como `DELETED` (exclusão lógica) |
| `ChangeStatusDocumentService` | Altera o status para qualquer valor válido do enum |
| `DocumentStatsService` | Gera relatório diário agendado (todo dia à meia-noite) |

---

## 🔐 Segurança e Autenticação

O VEXYN utiliza **dupla camada de segurança** para as rotas de documentos:

### 1. API Key + API Secret

Todas as requisições para `/documents/**` exigem dois headers obrigatórios:

```http
X-Api-Key: <sua-api-key>
X-Api-Secret: <seu-api-secret>
```

O filtro `ApiKeyFilter` intercepta a requisição e valida os headers **antes** do filtro JWT. Caso inválidos, retorna:

```json
{ "error": "Invalid or missing API key." }
```

> **Rotas excluídas** do filtro de API Key: `/users/**`, `/swagger-ui/**`, `/v3/api-docs/**`

### 2. Bearer JWT Token

Além da API Key, as rotas protegidas exigem um token JWT no header `Authorization`:

```http
Authorization: Bearer <token>
```

O token é gerado pelo endpoint `POST /users/auth` e contém:
- **Subject**: UUID do usuário
- **Claim `name`**: nome do usuário
- **Claim `roles`**: perfil do usuário (`ADMIN`, `MANAGER` ou `USER`)
- **Expiração**: 7 dias

### 3. Controle de Acesso por Perfil (`@PreAuthorize`)

| Endpoint | Perfis permitidos |
|---|---|
| `POST /documents/upload` | `ADMIN`, `MANAGER` |
| `GET /documents` | `ADMIN`, `MANAGER`, `USER` |
| `DELETE /documents/{id}` | `ADMIN`, `MANAGER` |
| `PUT /documents/{id}/update` | `ADMIN`, `MANAGER` |
| `PATCH /documents/{id}/status` | `ADMIN`, `MANAGER` |

### 4. Rotas públicas (sem autenticação)

| Endpoint | Descrição |
|---|---|
| `POST /users` | Criação de usuário |
| `POST /users/auth` | Autenticação e obtenção de JWT |
| `/swagger-ui/**` | Interface Swagger |
| `/v3/api-docs/**` | Spec OpenAPI |

---

## 📊 Logging e Monitoramento

O projeto usa **Log4j2** com modo **assíncrono** (LMAX Disruptor) para alta performance.

### Appenders configurados (`log4j2.xml`)

| Appender | Arquivo | Conteúdo |
|---|---|---|
| `LogToConsole` | Console (stdout) | Todos os logs |
| `UploadAuditFile` | `logs/uploads.log` | Ações de upload e manipulação de documentos |
| `LogToDatabaseFile` | `logs/database.log` | Queries SQL e bind parameters do Hibernate |
| `LogAuditDayFile` | `logs/auditDay.log` | Auditoria diária de ações |

### Relatório Diário Automático

O `DocumentStatsService` executa automaticamente todo dia à meia-noite (`0 0 0 * * *`) e gera um log com:

```
[RELATÓRIO DIÁRIO] Total de arquivos: 42 | Espaço total: 104857600 bytes (100.00 MB)
```

### Exemplo de log de upload

```
14:32:11.452 [http-nio-8080-exec-1] INFO  DocumentController - User [joao] starting file upload: relatorio.pdf
14:32:11.891 [http-nio-8080-exec-1] INFO  DocumentController - User [joao] - Document sent successfully. ID: 550e8400-e29b-41d4-a716-446655440000
```

---

## 📡 Documentação dos Endpoints

> 💡 A documentação interativa completa está disponível em: `http://localhost:8080/swagger-ui.html`

---

### 👤 Usuários (`/users`)

#### `POST /users` — Criar usuário

Cria um novo usuário na plataforma. Esta rota é **pública** (não requer autenticação).

**Request Body:**
```json
{
  "username": "João Silva",
  "email": "joao@email.com",
  "password": "senha123",
  "position": "MANAGER"
}
```

> `position` aceita: `ADMIN`, `MANAGER`, `USER`

**Respostas:**

| Código | Descrição |
|---|---|
| `201 Created` | Usuário criado com sucesso |
| `400 Bad Request` | Dados inválidos (campo obrigatório ausente, e-mail inválido, senha curta) |
| `409 Conflict` | E-mail já cadastrado |

---

#### `POST /users/auth` — Autenticar usuário

Valida as credenciais e retorna um token JWT. Esta rota é **pública**.

**Request Body:**
```json
{
  "email": "joao@email.com",
  "password": "senha123"
}
```

**Resposta de sucesso (`200 OK`):**
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI1NTBlO...
```

> O token retornado deve ser enviado no header `Authorization: Bearer <token>` nas demais requisições.

**Respostas:**

| Código | Descrição |
|---|---|
| `200 OK` | Token gerado com sucesso |
| `401 Unauthorized` | Credenciais inválidas |
| `400 Bad Request` | Erro inesperado |

---

### 📄 Documentos (`/documents`)

> ⚠️ **Todos os endpoints abaixo exigem** os headers `X-Api-Key` e `X-Api-Secret`, além do `Authorization: Bearer <token>`.

---

#### `POST /documents/upload` — Upload de documento

Envia um arquivo ao servidor e salva os metadados no banco.

**Perfis:** `ADMIN`, `MANAGER`

**Headers obrigatórios:**
```http
Authorization: Bearer <token>
X-Api-Key: 123456
X-Api-Secret: 654321
Content-Type: multipart/form-data
```

**Form Data:**
```
file: <arquivo>
```

**Resposta de sucesso (`201 Created`):**
```
550e8400-e29b-41d4-a716-446655440000
```

> O corpo da resposta é o UUID do documento recém-criado.

**Respostas:**

| Código | Descrição |
|---|---|
| `201 Created` | Documento enviado com sucesso |
| `400 Bad Request` | Erro ao processar o arquivo |
| `401 Unauthorized` | Token ou API Key inválidos |
| `403 Forbidden` | Perfil sem permissão |

---

#### `GET /documents` — Listar documentos

Retorna todos os documentos cadastrados que **não** estejam com status `DELETED`.

**Perfis:** `ADMIN`, `MANAGER`, `USER`

**Headers obrigatórios:**
```http
Authorization: Bearer <token>
X-Api-Key: 123456
X-Api-Secret: 654321
```

**Resposta de sucesso (`200 OK`):**
```json
[
  {
    "documentId": "550e8400-e29b-41d4-a716-446655440000",
    "documentName": "relatorio.pdf",
    "documentPath": "/home/user/upload/relatorio.pdf",
    "status": "PROCESSING",
    "data": null,
    "createdAt": "2025-03-01T10:30:00",
    "updatedAt": "2025-03-01T10:30:00"
  }
]
```

**Respostas:**

| Código | Descrição |
|---|---|
| `200 OK` | Lista retornada com sucesso |
| `400 Bad Request` | Erro ao listar documentos |
| `401 Unauthorized` | Token ou API Key inválidos |

---

#### `DELETE /documents/{id}` — Excluir documento

Realiza a **exclusão lógica** do documento (muda o status para `DELETED`).

**Perfis:** `ADMIN`, `MANAGER`

**Parâmetros de path:**

| Parâmetro | Tipo | Descrição |
|---|---|---|
| `id` | `UUID` | ID do documento a ser excluído |

**Headers obrigatórios:**
```http
Authorization: Bearer <token>
X-Api-Key: 123456
X-Api-Secret: 654321
```

**Resposta de sucesso:** `204 No Content` (sem corpo)

**Respostas:**

| Código | Descrição |
|---|---|
| `204 No Content` | Documento excluído com sucesso |
| `400 Bad Request` | Documento não encontrado |
| `401 Unauthorized` | Token ou API Key inválidos |
| `403 Forbidden` | Perfil sem permissão |

---

#### `PUT /documents/{id}/update` — Atualizar arquivo

Substitui o conteúdo do arquivo de um documento existente. O status é alterado para `UPDATED` se o conteúdo for diferente.

**Perfis:** `ADMIN`, `MANAGER`

**Parâmetros de path:**

| Parâmetro | Tipo | Descrição |
|---|---|---|
| `id` | `UUID` | ID do documento a ser atualizado |

**Headers obrigatórios:**
```http
Authorization: Bearer <token>
X-Api-Key: 123456
X-Api-Secret: 654321
Content-Type: multipart/form-data
```

**Form Data:**
```
file: <novo arquivo>
```

**Resposta de sucesso:** `200 OK` (sem corpo)

**Respostas:**

| Código | Descrição |
|---|---|
| `200 OK` | Arquivo atualizado com sucesso |
| `400 Bad Request` | Documento não encontrado ou erro no arquivo |
| `401 Unauthorized` | Token ou API Key inválidos |
| `403 Forbidden` | Perfil sem permissão |

---

#### `PATCH /documents/{id}/status` — Alterar status

Atualiza o status de um documento para qualquer valor válido do enum.

**Perfis:** `ADMIN`, `MANAGER`

**Parâmetros de path:**

| Parâmetro | Tipo | Descrição |
|---|---|---|
| `id` | `UUID` | ID do documento |

**Headers obrigatórios:**
```http
Authorization: Bearer <token>
X-Api-Key: 123456
X-Api-Secret: 654321
Content-Type: application/json
```

**Request Body:**
```json
{
  "status": "READY"
}
```

> `status` aceita: `PROCESSING`, `READY`, `UPDATED`, `DELETED`

**Resposta de sucesso:** `204 No Content` (sem corpo)

**Respostas:**

| Código | Descrição |
|---|---|
| `204 No Content` | Status atualizado com sucesso |
| `400 Bad Request` | Documento não encontrado ou status inválido |
| `401 Unauthorized` | Token ou API Key inválidos |
| `403 Forbidden` | Perfil sem permissão |

---

## ▶️ Como Executar o Projeto

### ✅ Requisitos

- Java 21+
- Maven 3.8+
- Docker e Docker Compose

### 1. Clonar o repositório

```bash
git clone https://github.com/DevKaiPereira/kaiquesouzapereiraAvaliacao.git
cd kaiquesouzapereiraAvaliacao
```

### 2. Subir o banco de dados com Docker

```bash
docker-compose up -d
```

Isso cria um container MySQL com:
- Host: `localhost:3306`
- Database: `documentsDb`
- Usuário: `docker`
- Senha: `docker`

### 3. Configurar as variáveis de ambiente

Edite o arquivo `src/main/resources/application.properties` conforme necessário (veja a seção [Variáveis de Ambiente](#-variáveis-de-ambiente)).

### 4. Compilar e executar

```bash
./mvnw spring-boot:run
```

Ou, no Windows:

```bash
mvnw.cmd spring-boot:run
```

### 5. Acessar a documentação Swagger

Abra no navegador:

```
http://localhost:8080/swagger-ui.html
```

---

## ⚙️ Variáveis de Ambiente

Todas as configurações ficam em `src/main/resources/application.properties`:

| Propriedade | Valor padrão | Descrição |
|---|---|---|
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/documentsDb` | URL do banco de dados |
| `spring.datasource.username` | `docker` | Usuário do banco |
| `spring.datasource.password` | `docker` | Senha do banco |
| `spring.jpa.hibernate.ddl-auto` | `update` | Estratégia DDL do Hibernate |
| `spring.servlet.multipart.max-file-size` | `10MB` | Tamanho máximo de arquivo |
| `spring.servlet.multipart.max-request-size` | `10MB` | Tamanho máximo da requisição |
| `api.key` | `123456` | Chave de acesso à API |
| `api.secret` | `654321` | Segredo de acesso à API |
| `security.token.secret` | `dkfjfdslkjdnlsdsaldldfnsljd` | Segredo para assinar os JWTs |
| `logging.config` | `classpath:log4j2.xml` | Configuração do Log4j2 |

> ⚠️ **Atenção:** Em produção, **nunca** deixe `api.key`, `api.secret` e `security.token.secret` com valores padrão. Use variáveis de ambiente do sistema operacional ou um vault de segredos.

---

## 🧪 Testes

O projeto possui testes unitários para os principais serviços, usando **JUnit 5** e **Mockito** (via `spring-boot-starter-test`).

### Testes disponíveis

| Classe de Teste | Serviço testado |
|---|---|
| `CreateUserServiceTest` | Criação de usuário |
| `AuthenticateUserServiceTest` | Autenticação de usuário |
| `UploadDocumentServiceTest` | Upload de documento |
| `FetchAllDocumentServiceTest` | Listagem de documentos |
| `UpdateDocumentServiceTest` | Atualização de arquivo |
| `DeleteDocumentServiceTest` | Exclusão lógica de documento |
| `ChangeStatusDocumentServiceTest` | Mudança de status |

### Executar todos os testes

```bash
./mvnw test
```

### Executar um teste específico

```bash
./mvnw test -Dtest=UploadDocumentServiceTest
```

---

## 🗺️ Roadmap

### ✅ Funcionalidades atuais

- [x] Upload de documentos (multipart)
- [x] Listagem de documentos (excluindo deletados)
- [x] Atualização de arquivo existente
- [x] Exclusão lógica (soft delete)
- [x] Mudança de status de documentos
- [x] Autenticação com JWT
- [x] Controle de acesso por perfil (ADMIN/MANAGER/USER)
- [x] Dupla autenticação: JWT + API Key/Secret
- [x] Logging estruturado com Log4j2
- [x] Relatório diário automático agendado
- [x] Documentação Swagger/OpenAPI 3
- [x] Testes unitários de serviços

### 🔮 Funcionalidades futuras

- [ ] Endpoint de download de documentos
- [ ] Paginação e filtros na listagem
- [ ] Integração com armazenamento em nuvem (S3, GCS)
- [ ] Refresh Token para JWT
- [ ] Soft delete de usuários
- [ ] Endpoint de atualização de perfil de usuário
- [ ] Rate limiting por usuário
- [ ] Testes de integração com banco em memória (H2)
- [ ] Pipeline CI/CD com GitHub Actions
- [ ] Deploy containerizado (Dockerfile da aplicação)

---

<div align="center">

Feito por **Kaique Souza Pereira**

</div>
