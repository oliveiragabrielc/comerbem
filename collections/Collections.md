# Collections para Teste da API ComerBem

## Visão Geral

Este documento apresenta uma coleção completa de testes para a API do projeto ComerBem, organizados por funcionalidade e incluindo cenários de sucesso, erro e casos extremos. As collections estão estruturadas para facilitar o teste manual e automatizado da API.

### Estrutura das Collections

```
ComerBem API Collections
├── 🔐 Autenticação
│   ├── Login Usuário
│   ├── Alterar Senha
│   └── Validar Token
├── 👤 Gerenciamento de Usuários
│   ├── Cadastrar Usuário
│   ├── Consultar Usuário
│   ├── Atualizar Usuário
│   ├── Listar Usuários
│   └── Deletar Usuário
├── 🏪 Gerenciamento de Restaurantes
│   ├── Cadastrar Restaurante
│   ├── Consultar Restaurante
│   ├── Atualizar Restaurante
│   ├── Listar Restaurantes
│   └── Deletar Restaurante
├── 🍽️ Gerenciamento de Cardápio
│   ├── Cadastrar Item
│   ├── Consultar Item
│   ├── Atualizar Item
│   ├── Listar Itens
│   ├── Listar por Restaurante
│   └── Deletar Item
└── 🧪 Cenários de Teste
    ├── Fluxo Completo
    ├── Testes de Validação
    └── Testes de Segurança
```

### Configuração Inicial

#### Variáveis de Ambiente
Antes de executar as collections, configure as seguintes variáveis no Postman:

```json
{
  "baseUrl": "http://localhost:8080",
  "token": "",
  "userId": "",
  "restauranteId": "",
  "itemId": ""
}
```

#### Headers Globais
```json
{
  "Content-Type": "application/json",
  "Accept": "application/json"
}
```

## 🔐 Autenticação (LoginController)

### 1.1. Login de Usuário

**Endpoint:** `POST {{baseUrl}}/login`

**Descrição:** Autentica um usuário no sistema e retorna um token JWT para autorização em endpoints protegidos.

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
  "login": "joao.silva",
  "senha": "senha123"
}
```


**Resposta de Sucesso (200 OK):**
```json
{
  "usuario": "joao.silva",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2FvLnNpbHZhIiwiaWF0IjoxNjkxNTIwMDAwLCJleHAiOjE2OTE2MDY0MDB9.signature"
}
```

**Resposta de Erro (400 Bad Request):**
```json
{
  "timestamp": "2025-08-05T19:00:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Usuário não encontrado.",
  "path": "/login"
}
```

**Resposta de Erro (401 Unauthorized):**
```json
{
  "timestamp": "2025-08-05T19:00:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Credenciais inválidas.",
  "path": "/login"
}
```

### 1.2. Alterar Senha

**Endpoint:** `PUT {{baseUrl}}/login/trocar`

**Descrição:** Permite que um usuário altere sua senha fornecendo a senha atual e a nova senha.

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{token}}
```

**Body (raw JSON):**
```json
{
  "login": "joao.silva",
  "email": "joao@email.com",
  "senhaAtual": "senha123",
  "novaSenha": "novaSenha456"
}
```


**Resposta de Sucesso (200 OK):**
```json
{
  "message": "Senha alterada com sucesso",
  "timestamp": "2025-08-05T19:00:00.000+00:00"
}
```

### 1.3. Validar Token

**Endpoint:** `GET {{baseUrl}}/login/validar`

**Descrição:** Valida se o token JWT ainda é válido e retorna informações do usuário.

**Headers:**
```
Authorization: Bearer {{token}}
```


## 👤 Gerenciamento de Usuários (UsuarioController)

### 2.1. Cadastrar Usuário

**Endpoint:** `POST {{baseUrl}}/usuario`

**Descrição:** Cadastra um novo usuário no sistema.

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
  "nome": "João Silva",
  "login": "joao.silva",
  "email": "joao@email.com",
  "senha": "senha123",
  "tipo": "PROPRIETARIO"
}
```


**Resposta de Sucesso (201 Created):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "login": "joao.silva",
  "email": "joao@email.com",
  "tipo": "PROPRIETARIO",
  "dataCriacao": "2025-08-05T19:00:00",
  "dataAtualizacao": "2025-08-05T19:00:00"
}
```

**Resposta de Erro (400 Bad Request) - Login já existe:**
```json
{
  "timestamp": "2025-08-05T19:00:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Login já está sendo utilizado.",
  "path": "/usuario"
}
```

**Resposta de Erro (400 Bad Request) - Email já existe:**
```json
{
  "timestamp": "2025-08-05T19:00:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Email já está sendo utilizado.",
  "path": "/usuario"
}
```

### 2.2. Consultar Usuário por ID

**Endpoint:** `GET {{baseUrl}}/usuario/{{userId}}`

**Descrição:** Recupera os dados de um usuário específico pelo ID.

**Headers:**
```
Authorization: Bearer {{token}}
```


**Resposta de Sucesso (200 OK):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "login": "joao.silva",
  "email": "joao@email.com",
  "tipo": "PROPRIETARIO",
  "dataCriacao": "2025-08-05T19:00:00",
  "dataAtualizacao": "2025-08-05T19:00:00"
}
```

### 2.3. Atualizar Usuário

**Endpoint:** `PUT {{baseUrl}}/usuario`

**Descrição:** Atualiza os dados de um usuário existente.

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{token}}
```

**Body (raw JSON):**
```json
{
  "id": 1,
  "nome": "João Silva Santos",
  "login": "joao.silva",
  "email": "joao.santos@email.com",
  "tipo": "PROPRIETARIO"
}
```

### 2.4. Listar Usuários

**Endpoint:** `GET {{baseUrl}}/usuario`

**Descrição:** Lista todos os usuários cadastrados (apenas para administradores).

**Headers:**
```
Authorization: Bearer {{token}}
```

**Query Parameters:**
```
page=0
size=10
sort=nome,asc
```

### 2.5. Deletar Usuário

**Endpoint:** `DELETE {{baseUrl}}/usuario/{{userId}}`

**Descrição:** Remove um usuário do sistema.

**Headers:**
```
Authorization: Bearer {{token}}
```

## 🏪 Gerenciamento de Restaurantes (RestauranteController)

### 3.1. Cadastrar Restaurante

**Endpoint:** `POST {{baseUrl}}/restaurante`

**Descrição:** Cadastra um novo restaurante no sistema.

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{token}}
```

**Body (raw JSON):**
```json
{
  "nome": "Restaurante Bella Vista",
  "endereco": {
    "rua": "Rua das Flores, 123",
    "numero": "123",
    "cidade": "São Paulo",
    "estado": "SP"
  },
  "tipoCozinha": "PIZZA",
  "horarioFuncionamento": {
    "abertura": "08:00",
    "fechamento": "22:00"
  },
  "usuario": {
    "id": 1,
    "nome": "João Silva",
    "tipo": "PROPRIETARIO"
  }
}
```

**Resposta de Sucesso (201 Created):**
```json
{
  "id": 1,
  "nome": "Restaurante Bella Vista",
  "endereco": {
    "rua": "Rua das Flores, 123",
    "numero": "123",
    "cidade": "São Paulo",
    "estado": "SP"
  },
  "tipoCozinha": "PIZZA",
  "horarioFuncionamento": {
    "abertura": "08:00",
    "fechamento": "22:00"
  },
  "usuario": {
    "id": 1,
    "nome": "João Silva",
    "tipo": "PROPRIETARIO"
  },
  "dataCriacao": "2025-08-05T19:00:00",
  "dataAtualizacao": "2025-08-05T19:00:00"
}
```

**Resposta de Erro (400 Bad Request) - Nome já existe:**
```json
{
  "timestamp": "2025-08-05T19:00:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "O nome do restaurante já está sendo utilizado!",
  "path": "/restaurante"
}
```

### 3.2. Consultar Restaurante por ID

**Endpoint:** `GET {{baseUrl}}/restaurante/{{restauranteId}}`

**Descrição:** Recupera os dados de um restaurante específico pelo ID.

**Headers:**
```
Authorization: Bearer {{token}}
```

### 3.3. Atualizar Restaurante

**Endpoint:** `PUT {{baseUrl}}/restaurante`

**Descrição:** Atualiza os dados de um restaurante existente.

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{token}}
```

**Body (raw JSON):**
```json
{
  "id": 1,
  "nome": "Restaurante Bella Vista Premium",
  "endereco": {
    "rua": "Rua das Flores, 456",
    "numero": "456",
    "cidade": "São Paulo",
    "estado": "SP"
  },
  "tipoCozinha": "CHINESA",
  "horarioFuncionamento": {
    "abertura": "09:00",
    "fechamento": "23:00"
  },
  "usuario": {
    "id": 1,
    "nome": "João Silva",
    "tipo": "PROPRIETARIO"
  }
}
```

### 3.4. Listar Restaurantes

**Endpoint:** `GET {{baseUrl}}/restaurante`

**Descrição:** Lista todos os restaurantes cadastrados.

**Headers:**
```
Authorization: Bearer {{token}}
```

**Query Parameters:**
```
page=0
size=10
tipoCozinha=PIZZA
cidade=São Paulo
```

### 3.5. Deletar Restaurante

**Endpoint:** `DELETE {{baseUrl}}/restaurante/{{restauranteId}}`

**Descrição:** Remove um restaurante do sistema.

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{token}}
```

**Body (raw JSON):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "tipo": "PROPRIETARIO"
}
```

## 🍽️ Gerenciamento de Cardápio (ItemController)

### 4.1. Cadastrar Item do Cardápio

**Endpoint:** `POST {{baseUrl}}/cardapio`

**Descrição:** Cadastra um novo item no cardápio de um restaurante.

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{token}}
```

**Body (raw JSON):**
```json
{
  "nome": "Pizza Margherita",
  "descricao": "Pizza tradicional com molho de tomate, mussarela e manjericão",
  "preco": 25.90,
  "somenteNoLocal": false,
  "caminhoFoto": "/images/pizza-margherita.jpg",
  "restauranteId": 1
}
```


**Resposta de Sucesso (201 Created):**
```json
{
  "id": 1,
  "nome": "Pizza Margherita",
  "descricao": "Pizza tradicional com molho de tomate, mussarela e manjericão",
  "preco": 25.90,
  "somenteNoLocal": false,
  "caminhoFoto": "/images/pizza-margherita.jpg",
  "restauranteId": 1,
  "dataCriacao": "2025-08-05T19:00:00",
  "dataAtualizacao": "2025-08-05T19:00:00"
}
```

**Resposta de Erro (400 Bad Request) - Nome vazio:**
```json
{
  "timestamp": "2025-08-05T19:00:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "O nome do item não pode ser vazio.",
  "path": "/cardapio"
}
```

**Resposta de Erro (400 Bad Request) - Preço negativo:**
```json
{
  "timestamp": "2025-08-05T19:00:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "O preço do item não pode ser negativo.",
  "path": "/cardapio"
}
```

### 4.2. Listar Todos os Itens do Cardápio

**Endpoint:** `GET {{baseUrl}}/cardapio`

**Descrição:** Recupera todos os itens do cardápio de todos os restaurantes.

**Headers:**
```
Authorization: Bearer {{token}}
```

**Query Parameters:**
```
page=0
size=20
sort=preco,asc
```

**Resposta de Sucesso (200 OK):**
```json
[
  {
    "id": 1,
    "nome": "Pizza Margherita",
    "descricao": "Pizza tradicional com molho de tomate, mussarela e manjericão",
    "preco": 25.90,
    "somenteNoLocal": false,
    "caminhoFoto": "/images/pizza-margherita.jpg",
    "restauranteId": 1
  },
  {
    "id": 2,
    "nome": "Hambúrguer Artesanal",
    "descricao": "Hambúrguer com carne bovina, queijo cheddar e molho especial",
    "preco": 18.50,
    "somenteNoLocal": true,
    "caminhoFoto": "/images/hamburguer-artesanal.jpg",
    "restauranteId": 2
  }
]
```

### 4.3. Listar Itens do Cardápio por Restaurante

**Endpoint:** `GET {{baseUrl}}/cardapio/{{restauranteId}}`

**Descrição:** Recupera todos os itens do cardápio de um restaurante específico.

**Headers:**
```
Authorization: Bearer {{token}}
```
### 4.4. Atualizar Item do Cardápio

**Endpoint:** `PUT {{baseUrl}}/cardapio`

**Descrição:** Atualiza os dados de um item do cardápio existente.

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{token}}
```

**Body (raw JSON):**
```json
{
  "id": 1,
  "nome": "Pizza Margherita Premium",
  "descricao": "Pizza tradicional com molho de tomate, mussarela de búfala e manjericão fresco",
  "preco": 32.90,
  "somenteNoLocal": false,
  "caminhoFoto": "/images/pizza-margherita-premium.jpg",
  "restauranteId": 1
}
```

### 4.5. Deletar Item do Cardápio

**Endpoint:** `DELETE {{baseUrl}}/cardapio/{{itemId}}`

**Descrição:** Remove um item do cardápio.

**Headers:**
```
Authorization: Bearer {{token}}
```



## 🧪 Cenários de Teste

### 5.1. Fluxo Completo - Cadastro de Restaurante e Cardápio

**Descrição:** Cenário end-to-end que simula o fluxo completo de um proprietário cadastrando restaurante e itens do cardápio.

**Sequência de Execução:**

1. **Cadastrar Usuário Proprietário**
   ```json
   POST /usuario
   {
     "nome": "Maria Restaurante",
     "login": "maria.rest",
     "email": "maria@restaurante.com",
     "senha": "senha123",
     "tipo": "PROPRIETARIO"
   }
   ```

2. **Fazer Login**
   ```json
   POST /login
   {
     "login": "maria.rest",
     "senha": "senha123"
   }
   ```

3. **Cadastrar Restaurante**
   ```json
   POST /restaurante
   {
     "nome": "Cantina da Maria",
     "endereco": {
       "rua": "Rua da Cantina, 100",
       "numero": "100",
       "cidade": "São Paulo",
       "estado": "SP"
     },
     "tipoCozinha": "PIZZA",
     "horarioFuncionamento": {
       "abertura": "11:00",
       "fechamento": "23:00"
     },
     "usuario": {
       "id": "{{userId}}",
       "nome": "Maria Restaurante",
       "tipo": "PROPRIETARIO"
     }
   }
   ```

4. **Cadastrar Itens do Cardápio**
   ```json
   POST /cardapio
   {
     "nome": "Pizza Calabresa",
     "descricao": "Pizza com calabresa, cebola e azeitonas",
     "preco": 28.90,
     "somenteNoLocal": false,
     "caminhoFoto": "/images/pizza-calabresa.jpg",
     "restauranteId": "{{restauranteId}}"
   }
   ```

5. **Verificar Cardápio do Restaurante**
   ```
   GET /cardapio/{{restauranteId}}
   ```

### 5.2. Testes de Validação

#### 5.2.1. Validação de Dados Obrigatórios

**Cadastro de Usuário - Nome Vazio:**
```json
POST /usuario
{
  "nome": "",
  "login": "teste.vazio",
  "email": "teste@email.com",
  "senha": "senha123",
  "tipo": "PROPRIETARIO"
}
```
*Esperado: 400 Bad Request*

**Cadastro de Restaurante - Endereço Incompleto:**
```json
POST /restaurante
{
  "nome": "Restaurante Teste",
  "endereco": {
    "rua": "Rua Teste"
    // cidade e estado ausentes
  },
  "tipoCozinha": "PIZZA"
}
```
*Esperado: 400 Bad Request*

**Cadastro de Item - Preço Negativo:**
```json
POST /cardapio
{
  "nome": "Item Teste",
  "preco": -10.50,
  "restauranteId": 1
}
```
*Esperado: 400 Bad Request*

#### 5.2.2. Validação de Unicidade

**Login Duplicado:**
```json
POST /usuario
{
  "nome": "Usuário Duplicado",
  "login": "joao.silva", // Login já existente
  "email": "novo@email.com",
  "senha": "senha123",
  "tipo": "CLIENTE"
}
```
*Esperado: 400 Bad Request*

**Nome de Restaurante Duplicado:**
```json
POST /restaurante
{
  "nome": "Restaurante Bella Vista", // Nome já existente
  "endereco": {
    "rua": "Outra Rua, 456",
    "numero": "456",
    "cidade": "Rio de Janeiro",
    "estado": "RJ"
  },
  "tipoCozinha": "CHINESA"
}
```
*Esperado: 400 Bad Request*

### 5.3. Testes de Segurança

#### 5.3.1. Acesso sem Autenticação

**Tentar acessar endpoint protegido sem token:**
```
GET /restaurante/1
# Sem header Authorization
```
*Esperado: 401 Unauthorized*

#### 5.3.2. Token Inválido

**Usar token malformado:**
```
GET /restaurante/1
Authorization: Bearer token-invalido
```
*Esperado: 401 Unauthorized*

#### 5.3.3. Token Expirado

**Simular token expirado:**
```javascript
// Pre-request Script
pm.environment.set("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0ZSIsImV4cCI6MTYwMDAwMDAwMH0.signature");
```
*Esperado: 401 Unauthorized*

#### 5.3.4. Acesso a Recursos de Outros Usuários

**Proprietário tentando acessar restaurante de outro:**
```
DELETE /restaurante/999
Authorization: Bearer {{token}}
{
  "id": 1,
  "nome": "João Silva",
  "tipo": "PROPRIETARIO"
}
```
*Esperado: 403 Forbidden*

### 5.4. Testes de Performance

#### 5.4.1. Carga de Dados

## 📊 Dados de Teste

### 6.1. Usuários de Teste

```json
[
  {
    "nome": "João Silva",
    "login": "joao.silva",
    "email": "joao@email.com",
    "senha": "senha123",
    "tipo": "PROPRIETARIO"
  },
  {
    "nome": "Maria Santos",
    "login": "maria.santos",
    "email": "maria@email.com",
    "senha": "senha456",
    "tipo": "PROPRIETARIO"
  },
  {
    "nome": "Carlos Cliente",
    "login": "carlos.cliente",
    "email": "carlos@email.com",
    "senha": "senha789",
    "tipo": "CLIENTE"
  }
]
```

### 6.2. Restaurantes de Teste

```json
[
  {
    "nome": "Pizzaria Bella Vista",
    "endereco": {
      "rua": "Rua das Flores, 123",
      "numero": "123",
      "cidade": "São Paulo",
      "estado": "SP"
    },
    "tipoCozinha": "PIZZA",
    "horarioFuncionamento": {
      "abertura": "18:00",
      "fechamento": "23:00"
    }
  },
  {
    "nome": "Churrascaria Gaúcha",
    "endereco": {
      "rua": "Avenida Paulista, 1000",
      "numero": "1000",
      "cidade": "São Paulo",
      "estado": "SP"
    },
    "tipoCozinha": "CHURRASCARIA",
    "horarioFuncionamento": {
      "abertura": "11:00",
      "fechamento": "22:00"
    }
  },
  {
    "nome": "Restaurante Oriental",
    "endereco": {
      "rua": "Rua da Liberdade, 500",
      "numero": "500",
      "cidade": "São Paulo",
      "estado": "SP"
    },
    "tipoCozinha": "CHINESA",
    "horarioFuncionamento": {
      "abertura": "12:00",
      "fechamento": "22:30"
    }
  }
]
```

### 6.3. Itens de Cardápio de Teste

```json
[
  {
    "nome": "Pizza Margherita",
    "descricao": "Pizza tradicional com molho de tomate, mussarela e manjericão",
    "preco": 25.90,
    "somenteNoLocal": false,
    "caminhoFoto": "/images/pizza-margherita.jpg"
  },
  {
    "nome": "Pizza Pepperoni",
    "descricao": "Pizza com molho de tomate, mussarela e pepperoni",
    "preco": 28.90,
    "somenteNoLocal": false,
    "caminhoFoto": "/images/pizza-pepperoni.jpg"
  },
  {
    "nome": "Picanha na Brasa",
    "descricao": "Picanha grelhada na brasa, acompanha farofa e vinagrete",
    "preco": 45.90,
    "somenteNoLocal": true,
    "caminhoFoto": "/images/picanha-brasa.jpg"
  },
  {
    "nome": "Yakisoba de Carne",
    "descricao": "Macarrão oriental refogado com carne e legumes",
    "preco": 22.50,
    "somenteNoLocal": false,
    "caminhoFoto": "/images/yakisoba-carne.jpg"
  }
]
```
### Códigos de Status HTTP

| Código | Significado | Quando Usar |
|--------|-------------|-------------|
| 200 OK | Sucesso | Operações de consulta e atualização |
| 201 Created | Criado | Recursos criados com sucesso |
| 204 No Content | Sem conteúdo | Operações de exclusão bem-sucedidas |
| 400 Bad Request | Requisição inválida | Dados inválidos ou validações falharam |
| 401 Unauthorized | Não autorizado | Token inválido, ausente ou expirado |
| 403 Forbidden | Proibido | Usuário sem permissão para a operação |
| 404 Not Found | Não encontrado | Recurso solicitado não existe |
| 409 Conflict | Conflito | Violação de regras de negócio (ex: duplicação) |
| 422 Unprocessable Entity | Entidade não processável | Dados válidos mas regras de negócio violadas |
| 500 Internal Server Error | Erro interno | Erro não tratado no servidor |

### Tipos de Dados

#### Enumerações
- **TipoCozinha**: `CHURRASCARIA`, `PIZZA`, `CHINESA`, `ARABE`
- **TipoUsuario**: `PROPRIETARIO`, `CLIENTE`

#### Formatos
- **Data/Hora**: ISO 8601 (`2025-08-05T19:00:00`)
- **Horário**: HH:mm (`08:00`, `22:30`)
- **Preço**: Decimal com 2 casas (`25.90`)

### Validações Importantes

1. **Autenticação**: Todos os endpoints exceto `/login` e `POST /usuario` requerem token JWT
2. **Autorização**: Proprietários só podem gerenciar seus próprios restaurantes
3. **Unicidade**: Login, email e nome de restaurante devem ser únicos
4. **Validações de Negócio**: 
   - Nome não pode ser vazio
   - Preço não pode ser negativo
   - Email deve ter formato válido
   - Horário de abertura deve ser anterior ao fechamento

### Dicas de Uso

1. **Ordem de Execução**: Sempre executar login antes de endpoints protegidos
2. **Limpeza**: Limpar dados de teste após execução para evitar conflitos
3. **Monitoramento**: Acompanhar tempo de resposta e tamanho das respostas
4. **Logs**: Verificar logs da aplicação em caso de erros 500
5. **Ambiente**: Usar ambiente de desenvolvimento/teste, nunca produção

Esta documentação fornece uma base completa para testar todas as funcionalidades da API ComerBem, incluindo cenários de sucesso, erro e casos extremos.

