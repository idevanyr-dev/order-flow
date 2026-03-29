# Planejamento do projeto — Order Flow

Este documento existe para servir como **guia de continuidade do projeto**.

A ideia é simples: em um novo chat, este arquivo deve permitir retomar o trabalho sem perder contexto, direção arquitetural, regras de colaboração e estado atual da implementação.

---

# 1. Visão geral do projeto

## 1.1. Nome do projeto

**Order Flow**

## 1.2. Objetivo

Construir um projeto de estudo prático com foco em:

- Java moderno
- Spring Boot moderno
- Spring Web
- Spring Data JDBC
- PostgreSQL
- Gradle Groovy DSL
- arquitetura modular orientada a domínio

## 1.3. Intenção pedagógica

Este projeto não é apenas uma API funcional.

Ele foi pensado como um **projeto evolutivo por capítulos**, onde cada etapa deve:

1. explicar a decisão arquitetural
2. refletir essa decisão no código real
3. manter o projeto legível, progressivo e sustentável

## 1.4. Princípios centrais

- evitar CRUD genérico
- evitar classes com nomes vagos como `Service`, `Manager` e `Processor`
- nomear por intenção de negócio
- separar leitura e escrita
- manter o domínio protegido
- tratar infraestrutura como detalhe isolado
- evoluir em passos pequenos, mas consistentes

---

# 2. Stack do projeto

## 2.1. Stack principal

- Java 25
- Spring Boot 4.x
- Spring Web
- Spring Data JDBC
- PostgreSQL
- Gradle Groovy DSL

## 2.2. Estilo arquitetural

O projeto deve seguir um **monólito modular orientado a domínio**.

Isso significa:

- um único deploy
- uma única base de código
- separação interna por módulos de negócio
- possibilidade de evolução arquitetural sem microserviços precoces

---

# 3. Organização estrutural esperada

Estrutura-base do módulo `order`:

```text
src/main/java/com/idevanyr/orderflow/order
├── api
├── application
├── domain
└── infrastructure
```

### Responsabilidades

#### `api`
- controllers
- requests HTTP
- responses HTTP
- tradução entre HTTP e aplicação

#### `application`
- casos de uso
- comandos
- resultados
- queries de aplicação
- orquestração

#### `domain`
- regras de negócio
- entidades
- value objects
- invariantes
- portas de domínio

#### `infrastructure`
- JDBC
- clientes HTTP externos
- mapeadores de persistência
- detalhes técnicos

---

# 4. Regra de trabalho importante para continuidade

## 4.1. Regra operacional acordada

**Sempre que for necessário alterar um arquivo já existente, a edição deve ser solicitada diretamente antes da mudança.**

Essa regra surgiu porque o fluxo de trabalho ficou melhor quando arquivos novos foram criados diretamente, mas arquivos já existentes passaram a exigir cuidado especial.

## 4.2. Como aplicar isso em novos chats

Se, em um novo chat, for necessário editar um arquivo já existente:

1. identificar claramente qual arquivo precisa ser alterado
2. pedir autorização para alteração desse arquivo
3. se necessário, fornecer o conteúdo completo esperado para o usuário colar
4. só depois continuar o capítulo ou a implementação

## 4.3. Regra prática adicional

Para **arquivos novos**, a criação pode seguir normalmente.

Para **arquivos existentes**, é melhor:

- avisar explicitamente qual arquivo será modificado
- mostrar o conteúdo novo quando isso reduzir risco
- pedir a edição antes de continuar

---

# 5. Estado atual do projeto

Este é o ponto mais importante para retomada futura.

## 5.1. O que já foi construído

### Capítulo 1 — Direção arquitetural
Já existe documentação da base arquitetural do projeto.

### Capítulo 2 — Modelagem inicial do módulo `order`
Já existe no código:

- `OrderStatus`
- `OrderItem`
- `Order`
- `OrderRepository`
- `OrderPolicy`
- `PlaceOrderCommand`
- `PlaceOrderItemCommand`
- `PlacedOrderResult`
- `PlaceOrderUseCase`
- `PlaceOrderUseCaseImpl`

### Capítulo 3 — Primeira API de criação de pedido
Já existe no código:

- `PlaceOrderController`
- `PlaceOrderRequest`
- `PlaceOrderItemRequest`
- `PlaceOrderResponse`
- `ErrorResponse`
- endpoint `POST /orders`

### Capítulo 4 — Persistência com Spring Data JDBC
Já existe no projeto:

- `OrderData`
- `OrderItemData`
- `OrderRecordMapper`
- `SpringDataJdbcOrderCrudRepository`
- `JdbcOrderRepository`
- `schema.sql`
- configuração de datasource no `application.yaml`

Observação: o capítulo 4 foi trabalhado em branch e depois levado adiante com PR.

### Capítulo 5 — Testes iniciais do módulo `order`
Já existem testes para:

- `OrderPolicy`
- `PlaceOrderUseCaseImpl`
- `PlaceOrderController`

### Capítulo 6 — Confirmação de pedido
Já existe no código:

- `OrderConfirmation`
- `ConfirmOrderCommand`
- `ConfirmOrderResult`
- `ConfirmOrderUseCase`
- `ConfirmOrderUseCaseImpl`
- `ConfirmOrderController`
- testes de confirmação

Importante: a regra de confirmação foi colocada corretamente no domínio, por meio do método `Order.confirm()`.

### Capítulo 7 — Cancelamento de pedido
Já existe no código:

- `OrderCancellation`
- `CancelOrderCommand`
- `CancelOrderResult`
- `CancelOrderUseCase`
- `CancelOrderUseCaseImpl`
- `CancelOrderController`
- testes de cancelamento

Importante: a regra de cancelamento foi colocada corretamente no domínio, por meio do método `Order.cancel()`.

## 5.2. Estado atual do domínio `Order`

O agregado `Order` já suporta pelo menos:

- criação via `place(...)`
- confirmação via `confirm()`
- cancelamento via `cancel()`
- cálculo de total

## 5.3. Estado atual da API

Endpoints já existentes:

- `POST /orders`
- `POST /orders/{orderId}/confirmation`
- `POST /orders/{orderId}/cancellation`

## 5.4. Estado atual dos testes

Já existe uma base inicial de testes unitários e de controller para:

- criação de pedido
- confirmação de pedido
- cancelamento de pedido

## 5.5. Situação geral

Neste momento, o projeto já tem:

- direção arquitetural consistente
- modelagem inicial do domínio
- API de escrita inicial
- persistência concreta com JDBC
- testes iniciais
- transições de estado de confirmação e cancelamento

O próximo passo mais natural é **consulta de pedido**.

---

# 6. Roadmap recomendado dos próximos capítulos

A sequência abaixo é a recomendada para continuar o projeto de forma coerente.

## 6.1. Próximo passo imediato

### Capítulo 8 — Consulta de pedido

Objetivo:

- criar endpoint de leitura por `id`
- criar query dedicada
- separar leitura de escrita de forma mais explícita
- começar a consolidar o CQRS-lite

Entregas esperadas:

- `FindOrderDetailsQuery`
- `OrderDetailsView`
- `OrderItemView`
- implementação JDBC de leitura
- `OrderQueryController`
- testes da consulta

## 6.2. Depois da consulta

### Capítulo 9 — Query model otimizado

Objetivo:

- melhorar leitura sem reconstruir todo o agregado
- introduzir projeções específicas para resposta da API
- retornar também total e quantidade total de itens

### Capítulo 10 — Pagamento do pedido

Objetivo:

- adicionar transição de pagamento
- impedir pagamento de pedido cancelado
- modelar resultado explícito da operação
- preparar regras de integração externa

### Capítulo 11 — Porta de pagamento

Objetivo:

- introduzir `PaymentGateway`
- desacoplar regra de negócio do provider externo
- preparar adaptadores dedicados

### Capítulo 12 — Adaptador de pagamento

Objetivo:

- criar implementação HTTP de integração
- manter contrato estável no domínio/aplicação
- tratar respostas externas de forma explícita

### Capítulo 13 — Migrations com Flyway

Objetivo:

- substituir inicialização simples por versionamento de schema
- preparar evolução segura do banco

### Capítulo 14 — Testes de integração da persistência

Objetivo:

- validar o repositório JDBC
- validar queries reais
- reduzir risco de inconsistência entre domínio e banco

### Capítulo 15 — Bean Validation e padronização HTTP

Objetivo:

- melhorar validação dos requests
- reduzir validações repetidas na borda HTTP
- padronizar respostas de erro

### Capítulo 16 — Tratamento global de erros

Objetivo:

- centralizar erros da API
- reduzir duplicação entre controllers

### Capítulo 17 — Observabilidade básica

Objetivo:

- adicionar logs mais intencionais
- health checks
- métricas iniciais
- uso melhor do Actuator

### Capítulo 18 — Notificações como porta

Objetivo:

- criar `NotificationGateway`
- isolar envio de notificações do domínio/aplicação

### Capítulo 19 — Concorrência e consistência

Objetivo:

- tratar atualização perdida
- discutir versionamento otimista
- preparar o sistema para concorrência real

### Capítulo 20 — Refino arquitetural geral

Objetivo:

- revisar fronteiras
- revisar nomes
- revisar o uso de `shared`
- revisar legibilidade e consistência do projeto

---

# 7. Prioridade prática recomendada

Se houver dúvida sobre qual direção seguir primeiro, esta é a ordem mais recomendada:

1. consulta de pedido
2. query model separado
3. pagamento
4. porta de pagamento
5. adaptador externo
6. migrations com Flyway
7. testes de integração
8. validação HTTP
9. tratamento global de erros
10. observabilidade

---

# 8. Regras arquiteturais que devem continuar valendo

## 8.1. Nomear por intenção

Preferir sempre nomes como:

- `PlaceOrderUseCase`
- `ConfirmOrderUseCase`
- `CancelOrderUseCase`
- `FindOrderDetailsQuery`

Evitar nomes genéricos como:

- `OrderService`
- `OrderManager`
- `OrderProcessor`

## 8.2. Manter resultados explícitos

Continuar usando `sealed interface` e resultados fechados quando fizer sentido.

## 8.3. Manter o domínio protegido

O domínio não deve depender de:

- controllers
- requests HTTP
- responses HTTP
- classes JDBC
- clientes HTTP externos

## 8.4. Persistência separada do domínio

Continuar usando:

- agregado de domínio limpo
- modelo de persistência separado
- mapper explícito

## 8.5. Continuar separando leitura e escrita

Tudo o que for consulta deve tender a seguir caminho de query dedicado.

---

# 9. Como retomar este projeto em um novo chat

Ao abrir um novo chat, a retomada ideal pode seguir este roteiro:

## 9.1. Passo 1
Dizer que o projeto atual é o `order-flow`.

## 9.2. Passo 2
Informar que o arquivo de referência de continuidade é:

- `docs/planejamento-do-projeto.md`

## 9.3. Passo 3
Informar o último estado funcional:

- criação de pedido pronta
- confirmação pronta
- cancelamento pronto
- persistência JDBC pronta
- testes iniciais prontos

## 9.4. Passo 4
Informar a próxima meta:

- consulta de pedido

## 9.5. Passo 5
Relembrar a regra operacional:

- se precisar editar arquivo já existente, pedir antes

---

# 10. Roteiro de retomada sugerido para o próximo chat

Texto sugerido para reabrir o trabalho:

> Estamos continuando o projeto `order-flow`. Leia o arquivo `docs/planejamento-do-projeto.md` e retome a partir do próximo passo recomendado. O estado atual é: criação, confirmação e cancelamento de pedido já existem; a próxima etapa é consulta de pedido. Lembre-se de pedir antes se precisar alterar algum arquivo já existente.

---

# 11. Critérios de qualidade para os próximos capítulos

Os próximos passos devem manter estes critérios:

- código legível
- nomes por intenção
- domínio com regra explícita
- controller fino
- aplicação orquestrando sem absorver regra indevida
- infraestrutura isolada
- testes acompanhando evolução
- documentação refletindo o código real

---

# 12. Conclusão

Este arquivo deve ser tratado como o **mapa principal de continuidade do projeto**.

Se houver qualquer dúvida em um próximo chat, a decisão padrão deve ser:

- consultar este documento
- verificar o estado atual do código
- continuar a partir do próximo passo recomendado
- preservar a regra de pedir antes de editar arquivos existentes
