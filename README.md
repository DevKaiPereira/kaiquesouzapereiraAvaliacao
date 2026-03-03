# Documentação do Projeto

👋 Bem-vindo à documentação do projeto!

## Descrição do Projeto
Este projeto é uma avaliação desenvolvida por Kaique Souza Pereira. A proposta é fornecer uma solução eficiente e bem estruturada para um desafio específico.

# 📄 VEXYN - Sistema de Gerenciamento de Documentos

## 📋 Descrição do Projeto

**VEXYN** é uma aplicação web moderna desenvolvida em **Java Spring Boot 3.5.11** para gerenciamento centralizado de documentos. O sistema permite que usuários autenticados façam upload, visualizem, atualizem e excluam documentos de forma segura, com controle de acesso baseado em papéis (RBAC).

A aplicação foi desenvolvida seguindo padrões de arquitetura limpa com separação de responsabilidades em módulos e implementa autenticação JWT com segurança Spring Security.

### 🎯 Objetivo Principal
Fornecer uma plataforma robusta e segura para gerenciamento de arquivos digitais com autenticação, autorização e rastreamento completo de operações.

---

## ✨ Funcionalidades Principais

### 👤 Módulo de Usuários
- **Criação de Usuários**: Registrar novos usuários no sistema com validação de email único
- **Autenticação JWT**: Gerar tokens JWT para acesso seguro à API
- **Controle de Papéis**: Três níveis de acesso - ADMIN, MANAGER e USER
- **Validação de Credenciais**: Senha criptografada com bcrypt

### 📁 Módulo de Documentos
- **Upload de Documentos**: Enviar arquivos com metadados (nome, tipo, data de criação)
- **Listagem de Documentos**: Visualizar todos os documentos ativos (excluindo documentos marcados como DELETED)
- **Atualização de Documentos**: Modificar conteúdo e metadados de documentos existentes
- **Exclusão Lógica**: Marcar documentos como DELETED sem removê-los fisicamente
- **Mudança de Status**: Alterar o status do documento (PROCESSING, READY, UPDATED, DELETED)
- **Armazenamento em Disco**: Salvar arquivos no disco local e metadados no banco de dados

### 🔒 Segurança
- Autenticação por JWT
- Autorização baseada em papéis (RBAC)
- Validação de entrada com Jakarta Validation
- Proteção contra múltiplas requisições com logging
- Tratamento de exceções customizado

### 📊 Logging e Monitoramento
- Log4j2 configurado para registrar todas as operações
- Arquivo de log diário: `logs/relatorio-diario.log`
- Rastreamento de ações por usuário
- Monitoramento de erros e exceções

---

## 🛣️ Estrutura de Rotas (API Endpoints)

### 1. **Módulo de Usuários** (`/users`)

#### Criar Novo Usuário
```http
POST /users
Content-Type: application/json

{
  "username": "novo_usuario",
  "email": "usuario@example.com",
  "password": "senha123",
  "position": "USER"
}ob a Licença MIT.
