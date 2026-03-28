# Order Flow

Projeto de estudo prático com **Java moderno**, **Spring Boot**, **Spring Web**, **Spring Data JDBC** e **Gradle Groovy DSL**.

A proposta deste repositório é evoluir um sistema chamado **Order Flow** como um **monólito modular orientado a domínio**, evitando tanto o CRUD genérico quanto a complexidade prematura de uma arquitetura excessiva.

## Objetivos do projeto

- praticar Java moderno com foco intermediário e avançado
- organizar o código por domínio e responsabilidade real
- separar leitura e escrita com intencionalidade
- usar Spring Data JDBC de forma explícita e saudável
- evoluir a arquitetura por capítulos, sempre refletindo no código real

## Stack principal

- Java 25
- Spring Boot 4.x
- Spring Web
- Spring Data JDBC
- PostgreSQL
- Gradle Groovy DSL

## Direção arquitetural

A aplicação será construída como um **monólito modular**, com separação interna por domínio.

Estrutura-base desejada:

```text
src/main/java/com/idevanyr/orderflow
├── shared
├── order
│   ├── api
│   ├── application
│   ├── domain
│   └── infrastructure
└── OrderFlowApplication.java
```

### Regras de organização

- `api`: controllers, requests e responses HTTP
- `application`: casos de uso, queries e orquestração
- `domain`: regras de negócio, entidades, value objects e portas
- `infrastructure`: persistência, integrações externas e detalhes técnicos
- `shared`: componentes reutilizáveis entre módulos, sem virar pasta genérica de utilidades

## Forma de evolução

Este repositório será desenvolvido em paralelo com os capítulos do curso.

Cada capítulo deve gerar:

1. documentação em `docs/`
2. código correspondente no projeto
3. commits que reflitam a evolução arquitetural e técnica

## Primeiros passos sugeridos

- consolidar a base arquitetural do módulo `order`
- modelar os primeiros casos de uso
- estruturar persistência com Spring Data JDBC
- introduzir testes de domínio antes de expandir endpoints

## Documentação

- [Capítulo 1 — Direção arquitetural do projeto](docs/capitulo-01-direcao-arquitetural.md)
