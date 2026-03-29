# Capítulo 4 — Persistência com Spring Data JDBC

Neste capítulo, o módulo `order` deixa de depender apenas de uma porta abstrata de persistência e ganha sua primeira implementação concreta com **Spring Data JDBC**.

---

## Objetivo do capítulo

Adicionar a persistência inicial do módulo `order`, mantendo o domínio protegido e a infraestrutura explicitamente separada.

Ao final deste capítulo, o projeto passa a ter:

- modelo de persistência JDBC separado do domínio
- mapeamento explícito entre domínio e dados
- repositório concreto para salvar e buscar pedidos
- base pronta para conectar banco relacional real

## Decisões do capítulo

### 1. O domínio continua sem anotações de persistência

As classes de domínio não recebem `@Table`, `@Id` ou detalhes técnicos de JDBC.

Esses detalhes ficam em `order.infrastructure.jdbc`.

### 2. O modelo de persistência é separado do agregado

Este capítulo introduz:

- `OrderData`
- `OrderItemData`
- `OrderRecordMapper`

Essa separação evita que o domínio vaze para o mecanismo de persistência.

### 3. O contrato continua sendo `OrderRepository`

A aplicação continua dependendo apenas da porta:

- `OrderRepository`

A implementação concreta passa a ser:

- `JdbcOrderRepository`

### 4. O mapeamento é explícito

Spring Data JDBC favorece clareza sobre agregado e composição.

Por isso, o capítulo usa um mapeador manual para:

- converter `Order` em `OrderData`
- converter `OrderData` em `Order`

## Estrutura adicionada neste capítulo

```text
src/main/java/com/idevanyr/orderflow/order/infrastructure/jdbc
├── JdbcOrderRepository.java
├── OrderData.java
├── OrderItemData.java
├── OrderRecordMapper.java
└── SpringDataJdbcOrderCrudRepository.java
```

## Papel de cada tipo

### `OrderData`
Representa o agregado persistido na tabela `orders`.

### `OrderItemData`
Representa os itens persistidos do pedido.

### `OrderRecordMapper`
Centraliza a tradução entre o modelo de domínio e o modelo JDBC.

### `SpringDataJdbcOrderCrudRepository`
Repositório Spring Data responsável pelas operações básicas do agregado persistido.

### `JdbcOrderRepository`
Implementa a porta `OrderRepository` usando Spring Data JDBC.

## O que ainda não entra neste capítulo

Para manter a evolução controlada, este capítulo ainda não adiciona:

- scripts de migração
- consultas especializadas de leitura
- versionamento otimista
- tratamento de concorrência
- testes de integração com banco

## Resultado esperado

Ao final deste capítulo, a arquitetura do módulo `order` fica mais completa:

- `application` depende de `domain`
- `domain` continua limpo
- `infrastructure` implementa persistência concreta
- a base já está pronta para receber schema e integração com PostgreSQL

## Próximo passo natural

Os próximos capítulos podem seguir para:

- migrations com Flyway
- query model separado para leitura
- testes de integração da persistência
- confirmação e cancelamento de pedido
