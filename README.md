# Order Flow

Projeto de estudo prático com **Java moderno**, **Spring Boot**, **Spring Web**, **Spring Data JDBC** e **Gradle Groovy DSL**.

A proposta deste repositório é evoluir um sistema chamado **Order Flow** como um **monólito modular orientado a domínio**, evitando tanto o CRUD genérico quanto a complexidade prematura de uma arquitetura excessiva.

## Modelo arquitetural adotado

Este projeto segue um **monólito modular orientado a domínio**.

Na prática, isso significa que a organização do código foi baseada em uma combinação pragmática de referências arquiteturais conhecidas, em vez de copiar uma escola específica de forma rígida.

### Base conceitual

A estrutura atual foi inspirada principalmente por:

- **DDD (Domain-Driven Design)**, na organização por domínio e na ênfase em linguagem de negócio explícita
- **Clean Architecture**, na separação entre núcleo de negócio e detalhes técnicos
- **Hexagonal Architecture**, no uso de portas e adaptadores para isolar infraestrutura
- **CQRS-lite**, na separação intencional entre operações de escrita e leitura

### O que isso significa no projeto

A organização por pastas:

```text
order/
├── api
├── application
├── domain
└── infrastructure
```

não foi escolhida por estética, mas para refletir responsabilidades reais:

- `api`: traduz HTTP para casos de uso e devolve respostas HTTP
- `application`: orquestra operações por intenção de negócio
- `domain`: concentra regras, invariantes e transições de estado
- `infrastructure`: implementa persistência e integrações técnicas

### Nomeação por intenção

Em vez de classes genéricas como:

- `OrderService`
- `OrderManager`
- `OrderProcessor`

o projeto prefere nomes como:

- `PlaceOrderUseCase`
- `ConfirmOrderUseCase`
- `CancelOrderUseCase`
- `FindOrderDetailsQuery`

Essa escolha vem principalmente de DDD e de arquiteturas orientadas a caso de uso, porque deixa mais claro **o que o sistema faz** do ponto de vista do negócio.

### O que este projeto não tenta ser

Este projeto não segue uma arquitetura “pura” no sentido acadêmico.

Ele não pretende ser:

- Hexagonal Architecture estrita
- Onion Architecture pura
- CQRS completo
- arquitetura tradicional em camadas genéricas como `controller/service/repository`
- microserviços

A proposta é mais simples e mais útil para este contexto:

> usar um desenho arquitetural limpo, modular e orientado a domínio, com baixa complexidade acidental e alta clareza de intenção.

### Definição curta

A melhor definição para este projeto é:

**DDD pragmático + Clean/Hexagonal em um monólito modular, com casos de uso explícitos e separação leve entre leitura e escrita.**

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
