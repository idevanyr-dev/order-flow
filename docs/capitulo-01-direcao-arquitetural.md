# Capítulo 1 — Direção arquitetural do projeto

Este capítulo define **como o projeto vai crescer sem virar um amontoado de controllers, services e repositories genéricos**. O foco aqui não é começar pelo “hello world”, mas por uma base que sustente evolução real com:

- **Java 25**
- **Spring Boot 4.0.4**
- **Spring Web**
- **Spring Data JDBC**
- **Gradle Groovy DSL**

A ideia é trabalhar com um **monólito modular**, orientado a domínio, com separação clara entre API, aplicação, domínio e infraestrutura.

---

## Objetivo do capítulo

Estabelecer a direção arquitetural do projeto `order-flow`, definindo:

- o domínio inicial
- as fronteiras entre camadas
- o estilo arquitetural da aplicação
- o impacto da stack nas decisões estruturais
- a base para os próximos capítulos

## Domínio inicial

O domínio de exemplo adotado é **Order Flow** (gestão de pedidos), porque ele permite trabalhar com:

- regras reais de negócio
- validação
- persistência relacional
- concorrência
- observabilidade
- evolução arquitetural

## Diretrizes principais

### 1. Modularizar por domínio

Evitar estruturas puramente em camadas genéricas como:

```text
controller/
service/
repository/
entity/
dto/
```

Preferir organização por domínio:

```text
com.idevanyr.orderflow
├── shared
└── order
    ├── api
    ├── application
    ├── domain
    └── infrastructure
```

### 2. Nomear por intenção

Evitar classes genéricas como `OrderService` quando a intenção real puder ser expressa com mais clareza:

- `PlaceOrderUseCase`
- `ConfirmOrderUseCase`
- `FindOrderDetailsQuery`
- `CancelOrderUseCase`

### 3. Separar leitura e escrita

O projeto deve distinguir desde cedo:

- **escrita**: operações com regras de negócio e consistência
- **leitura**: consultas otimizadas e projeções específicas

Isso prepara o código para um **CQRS-lite** sem complexidade desnecessária.

### 4. Proteger o domínio

O domínio não deve vazar detalhes de persistência ou HTTP. A tendência é usar:

- `record` para comandos, requests, responses e views
- `sealed interface` para resultados fechados
- portas explícitas para persistência e integrações

### 5. Tratar integrações como fronteiras

Chamadas a provedores externos não devem ficar misturadas na regra de negócio. O correto é expor portas estáveis, como:

- `PaymentGateway`
- `NotificationGateway`

com adaptadores concretos em `infrastructure/`.

## Estilo arquitetural escolhido

A estratégia inicial é um **monólito modular**.

Isso significa:

- um deploy
- uma base de código
- um banco relacional principal
- separação interna por módulos de negócio
- possibilidade de evoluir sem cair em microserviços precoces

## Estrutura sugerida

```text
src/main/java/com/idevanyr/orderflow
├── shared
│   ├── error
│   └── observability
├── order
│   ├── api
│   │   ├── PlaceOrderController.java
│   │   ├── ConfirmOrderController.java
│   │   ├── OrderQueryController.java
│   │   ├── PlaceOrderRequest.java
│   │   ├── PlaceOrderResponse.java
│   │   └── ErrorResponse.java
│   ├── application
│   │   ├── PlaceOrderUseCase.java
│   │   ├── PlaceOrderUseCaseImpl.java
│   │   ├── ConfirmOrderUseCase.java
│   │   ├── ConfirmOrderUseCaseImpl.java
│   │   ├── FindOrderDetailsQuery.java
│   │   └── OrderSummaryUseCase.java
│   ├── domain
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   ├── OrderStatus.java
│   │   ├── OrderRepository.java
│   │   ├── OrderPolicy.java
│   │   ├── OrderConfirmation.java
│   │   └── PaymentGateway.java
│   └── infrastructure
│       ├── jdbc
│       │   ├── OrderData.java
│       │   ├── OrderItemData.java
│       │   ├── OrderRecordMapper.java
│       │   ├── JdbcOrderRepository.java
│       │   └── SpringDataJdbcOrderCrudRepository.java
│       ├── query
│       │   └── JdbcFindOrderDetailsQuery.java
│       └── payment
│           └── HttpPaymentGateway.java
└── OrderFlowApplication.java
```

## O que este capítulo valida

Ao fim do capítulo 1, o projeto já deve ter clareza sobre:

- qual domínio será usado como base
- como o código será organizado
- como evitar arquitetura genérica e acoplamento acidental
- como a stack influencia as decisões
- como preparar evolução sem exagero estrutural

## Próximos passos

Os próximos capítulos devem transformar essa direção em código real, começando por:

- modelagem do domínio `order`
- primeiros casos de uso
- requests e responses HTTP
- persistência explícita com Spring Data JDBC
- testes de domínio

## Conclusão

A meta deste capítulo não é só “subir endpoint”, mas definir uma base sustentável para o crescimento do projeto.

A partir daqui, o repositório deve evoluir em duas frentes ao mesmo tempo:

- documentação em `docs/`
- implementação real no código
