# Documentação do Projeto ComerBem

Este documento detalha o projeto ComerBem, uma aplicação Spring Boot desenvolvida para atender a especificação de projeto da pos-graduação a instituição FIAP.

## 1. Arquitetura do Projeto

O projeto ComerBem segue uma arquitetura baseada em camadas, comum em aplicações Spring Boot, que promove a separação de responsabilidades e a modularidade. As principais camadas identificadas são:

*   **Controladores (Controllers):** Responsáveis por receber as requisições HTTP, orquestrar a lógica de negócio através dos serviços e retornar as respostas HTTP.
*   **Serviços (Services):** Contêm a lógica de negócio da aplicação, realizando operações complexas e coordenando as interações com o repositório.
*   **Repositórios (Repositories):** Responsáveis pela comunicação com o banco de dados, abstraindo a persistência dos dados.
*   **Modelos (Models/Entities):** Representam as entidades de domínio da aplicação, mapeadas para as tabelas do banco de dados.
*   **Casos de Uso (Use Cases):** (Identificado no projeto como `casodeuso`) Representam as operações de negócio de alto nível, encapsulando a lógica específica de cada funcionalidade.

O projeto utiliza o **Spring Boot** para facilitar o desenvolvimento de aplicações Java baseadas em microserviços, o **Spring Data JPA** para a persistência de dados e o **H2 Database** como banco de dados em memória para desenvolvimento e testes. A segurança é tratada com **JWT (JSON Web Tokens)** para autenticação.

## 2. Endpoints da API

Os endpoints da API são definidos nos controladores e expõem as funcionalidades da aplicação. Com base na estrutura do projeto, os seguintes controladores foram identificados:

*   `LoginController`: Responsável pela autenticação de usuários e operações relacionadas ao login.
*   `UsuarioController`: Responsável pelo gerenciamento de usuários (criação, consulta, atualização, exclusão).
*   `RestauranteController`: Responsável pelo gerenciamento de restaurantes (criação, consulta, atualização, exclusão).
*   `ItemController`: Responsável pelo gerenciamento o cardápio (criação, consulta, atualização, exclusão).
   
Para detalhes específicos sobre os endpoints (métodos HTTP, URLs, parâmetros de requisição e respostas), é recomendável consultar a documentação gerada pelo SpringDoc OpenAPI (Swagger UI), que estará disponível após a execução da aplicação.

## 3. Configuração e Execução

Para configurar e executar o projeto ComerBem, siga os passos abaixo:

### Pré-requisitos

Certifique-se de ter as seguintes ferramentas instaladas em sua máquina:

*   **Java Development Kit (JDK) 21** ou superior.
*   **Maven** (gerenciador de dependências do Java).
*   **Docker** e **Docker Compose** (para a configuração com Docker).

### 3.1. Execução Local (sem Docker)

1.  **Clone o repositório:**

    ```bash
    git clone [URL_DO_REPOSITORIO]
    cd comerbem
    ```

2.  **Compile o projeto:**

    Navegue até o diretório raiz do projeto (`/home/ubuntu/comerbem`) e execute o seguinte comando Maven para compilar o projeto e baixar as dependências:

    ```bash
    mvn clean install
    ```

3.  **Execute a aplicação:**

    Após a compilação bem-sucedida, você pode executar a aplicação Spring Boot:

    ```bash
    mvn spring-boot:run
    ```

    A aplicação estará acessível em `http://localhost:8080` (ou a porta configurada em `application.properties`). A interface do Swagger UI estará disponível em `http://localhost:8080/swagger-ui.html`.


## 4. Testes Unitários

O projeto já inclui uma estrutura básica de testes unitários.

Para rodar os teste execute

```
mvn test
```

## 4. Collections para Teste da API

Collections para Postman ou ferramenta similar se encontram na pasta collections.

## 1.1. Estrutura de Pacotes e Classes

O projeto está organizado nos seguintes pacotes principais, refletindo a separação de responsabilidades:

*   `br.com.fiap.comerbem.controller`: Contém as classes de controladores REST, que expõem os endpoints da API. Exemplos: `LoginController`, `UsuarioController`.
*   `br.com.fiap.comerbem.service`: Contém a lógica de negócio principal da aplicação. Exemplos: `AutenticarService`, `SenhaService`, `ValidarUsuarioService`.
*   `br.com.fiap.comerbem.repository`: Contém as interfaces de repositório para acesso a dados, estendendo `JpaRepository`. Exemplo: `UsuarioRepository`.
*   `br.com.fiap.comerbem.model`: Contém as classes de modelo de dados (entidades JPA). Exemplos: `Usuario`, `Endereco`.
*   `br.com.fiap.comerbem.casodeuso`: Contém as classes que implementam os casos de uso específicos da aplicação, divididos por funcionalidade. Exemplos: `GerenciarUsuario`, `Login`.
    *   `br.com.fiap.comerbem.casodeuso.gerenciarusuario.dto`: DTOs (Data Transfer Objects) para o caso de uso de gerenciamento de usuário. Exemplos: `UsuarioNovoDTO`, `UsuarioDTO`.
    *   `br.com.fiap.comerbem.casodeuso.login.dto`: DTOs para o caso de uso de login. Exemplos: `UsuarioLogado`, `UsuarioMudarSenha`, `UsuarioLoginDTO`.
*   `br.com.fiap.comerbem.config`: Contém classes de configuração da aplicação, como o `ExceptionHandlerConfig` para tratamento global de exceções.




### 3.2. Execução com Docker Compose

O projeto inclui uma configuração completa do Docker Compose que permite executar a aplicação junto com um banco de dados PostgreSQL.

#### Pré-requisitos para Docker

*   **Docker** e **Docker Compose** instalados na máquina.

#### Passos para execução

1.  **Navegue até o diretório do projeto:**

    ```bash
    cd /caminho/para/comerbem
    ```

2.  **Execute o Docker Compose:**

    ```bash
    docker-compose up --build
    ```

    Este comando irá:
    *   Construir a imagem Docker da aplicação Spring Boot
    *   Iniciar um container PostgreSQL com o banco de dados
    *   Iniciar a aplicação Spring Boot conectada ao PostgreSQL
    *   Iniciar o Adminer (interface web para administração do banco)

3.  **Acesse a aplicação:**

    *   **API:** `http://localhost:8080`
    *   **Swagger UI:** `http://localhost:8080/swagger-ui.html`
    *   **Adminer (Admin do Banco):** `http://localhost:8081`

4.  **Para parar os serviços:**

    ```bash
    docker-compose down
    ```

#### Configurações do Docker Compose

O arquivo `docker-compose.yml` inclui os seguintes serviços:

*   **postgres:** Banco de dados PostgreSQL 15
*   **comerbem-app:** Aplicação Spring Boot
*   **adminer:** Interface web para administração do banco de dados

## 6. Testes Unitários Implementados

O projeto agora inclui testes unitários abrangentes para as principais classes de serviço:

### 6.1. AutenticarServiceTest

Testa a funcionalidade de autenticação JWT, incluindo:
*   Geração de tokens válidos
*   Validação de tokens
*   Tratamento de tokens expirados
*   Tratamento de assinaturas inválidas
*   Tratamento de tokens malformados

### 6.2. SenhaServiceTest

Testa a funcionalidade de validação e criptografia de senhas:
*   Validação de senhas correspondentes
*   Tratamento de senhas não correspondentes
*   Criptografia de senhas

### 6.3. ValidarUsuarioServiceTest

Testa as operações de gerenciamento de usuários:
*   Criação de novos usuários
*   Alteração de usuários existentes
*   Exclusão de usuários
*   Busca de usuários por ID, login e email
*   Autenticação de usuários
*   Alteração de senhas

### 6.4. Executando os Testes

Para executar todos os testes unitários:

```bash
mvn test
```

Para executar testes com relatório de cobertura:

```bash
mvn test jacoco:report
```

## 7. Collections para Teste da API

O arquivo `POSTMAN_COLLECTION.md` contém exemplos detalhados de requisições para todos os endpoints da API, incluindo:

*   Autenticação de usuários
*   Alteração de senhas
*   Gerenciamento completo de usuários (CRUD)

Cada endpoint inclui exemplos de requisições e respostas de sucesso e erro.

## 8. Estrutura de Arquivos Adicionados

```
comerbem/
├── docker-compose.yml          # Configuração Docker Compose
├── Dockerfile                  # Imagem Docker da aplicação
├── .dockerignore              # Arquivos ignorados no build Docker
├── init-db.sql                # Script de inicialização do banco
├── POSTMAN_COLLECTION.md      # Collections para teste da API
├── src/
│   ├── main/
│   │   └── resources/
│   │       └── application-docker.properties  # Configurações para Docker
│   └── test/
│       └── java/
│           └── br/com/fiap/comerbem/service/
│               ├── AutenticarServiceTest.java
│               ├── SenhaServiceTest.java
│               └── ValidarUsuarioServiceTest.java
└── README.md                  # Esta documentação
```
### 6.4. GerenciarCardapioTest

Testa a funcionalidade de gerenciamento de itens de cardápio:
*   Cadastro de itens de cardápio
*   Alteração de itens de cardápio
*   Exclusão de itens de cardápio
*   Recuperação de itens de cardápio

### 6.5. GerenciarRestauranteTest

Testa a funcionalidade de gerenciamento de restaurantes:
*   Cadastro de restaurantes
*   Alteração de restaurantes
*   Exclusão de restaurantes
*   Recuperação de restaurantes

### 6.6. GerenciarUsuarioTest

Testa a funcionalidade de gerenciamento de usuários:
*   Cadastro de usuários
*   Alteração de usuários
*   Exclusão de usuários
*   Recuperação de usuários


