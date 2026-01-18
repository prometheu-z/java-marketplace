# Prometheu-Z: Marketplace

Um sistema de vendas em Java Puro via **CLI** onde usuários reais (ou bots) competem por estoque contra Bots alimentados pelo Google Gemini.

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)
![Gemini AI](https://img.shields.io/badge/Google%20Gemini-8E75B2?style=for-the-badge&logo=googlebard&logoColor=white)

## Sobre o Projeto

O foco principal é o gerenciamento de Concorrência e Condições de Corrida (Race Conditions) em um ambiente transacional.

### IA & Simulação
Diferente de um CRUD tradicional, este sistema simula um mercado vivo:
* Geração: Perfis de clientes, vendedores e produtos são criados via Google Gemini API.
* Bots Autônomos: O sistema instancia threads de bots que "acordam", analisam o mercado e efetuam compras em tempo real.
* Disputa de Estoque: O usuário compete com esses bots. O banco de dados garante (via transações ACID) que um item não seja vendido duas vezes no mesmo milissegundo.

## Tecnologias Utilizadas

* **Java 21**: Core da aplicação.
* **Docker & Docker Compose**: Containerização do Banco e da Aplicação.
* **JPA / Hibernate**: Gerenciamento manual do ciclo de vida de persistência.
* **MySQL 8**: Banco de dados relacional.
* **Maven**: Gerenciamento de dependências e Build.
* **Google Gemini API**: Inteligência Artificial Generativa.

## Arquitetura do Sistema

O projeto segue uma adaptação do padrão MVC focado em aplicações CLI e microsserviços simulados (Bots).

### Estrutura  

```mermaid
graph TD
    User([ Usuário ]) -->|Interage| View[ Camada View (CLI)]
    Bot([ Bot Gemini AI]) -->|Thread Paralela| Service
    
    subgraph Core Application
        View -->|Chama| Service[ Service Layer]
        Service -->|Regras de Negócio| Model[ Entidades ]
        Service -->|Persistência| DAO[ DAO Layer]
    end
    
    subgraph External Systems
        Service <-->|Gera Entidades| Gemini[ Google Gemini API]
        DAO <-->|SQL| MySQL[( MySQL Database)]
    end
```
---

## Como Rodar (Via Docker)

Siga os passos:

### 1. Pré-requisitos
* Ter o [Docker](https://www.docker.com/) instalado.
* Uma chave de API do [Google Gemini](https://aistudio.google.com/).
* Pasta com o clone do projeto ou .zip descompactado.

### 2. Configuração da API
Crie um arquivo chamado `config.properties` na raiz do projeto (onde está o `pom.xml`) e adicione sua chave:

gemini.api.key=SUA_CHAVE_AQUI

### 3. EXECUTAR

Abra o terminal na pasta do projeto e rode o comando abaixo. 

**docker compose run --service-ports app**

### 4. PARAR E LIMPAR

Para encerrar o banco de dados e limpar os containers criados:

**docker compose down**
