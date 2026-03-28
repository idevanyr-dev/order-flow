# Capítulo 2 — Modelagem inicial do módulo order

Neste capítulo, a direção arquitetural do capítulo 1 começa a virar código real.

O objetivo aqui não é implementar o sistema inteiro, mas consolidar a primeira fatia consistente do módulo `order` com foco em:

- modelagem do núcleo do domínio
- validação inicial de regras de colocação de pedido
- primeiro caso de uso nomeado por intenção
- contratos claros entre aplicação e domínio

---

## Objetivo do capítulo

Construir a primeira base executável do módulo `order`, com:

- `OrderStatus`
- `OrderItem`
- `Order`
- `OrderRepository`
- `OrderPolicy`
- `PlaceOrderUseCase`
- tipos de comando e resultado para colocação de pedido
- implementação inicial do caso de uso

## Decisões do capítulo

### 1. O domínio continua sem depender de HTTP

Nada de request, response, controller ou DTO HTTP neste momento.

A entrada do caso de uso será feita por comandos explícitos:

- `PlaceOrderCommand`
- `PlaceOrderItemCommand`

### 2. A validação de colocação de pedido começa por política

A intenção é evitar espalhar validação de negócio entre controller, service e entidade de persistência.

Por isso, este capítulo introduz `OrderPolicy` para validar regras como:

- cliente obrigatório
- pedido com pelo menos um item
- quantidade maior que zero
- preço unitário não negativo

### 3. O agregado nasce pequeno e intencional

A classe `Order` começa responsável por:

- representar o pedido
- nascer a partir do comando de colocação
- manter imutabilidade estrutural básica
- calcular total do pedido

### 4. O caso de uso fala por resultado explícito

Em vez de lançar exceções de validação como fluxo principal, o caso de uso retorna um resultado fechado:

- sucesso
- erro de validação

Isso prepara a aplicação para traduzir esses resultados para HTTP depois, sem vazar detalhes para o domínio.

## Estrutura adicionada neste capítulo

```text
src/main/java/com/idevanyr/orderflow/order
├── application
│   ├── PlaceOrderCommand.java
│   ├── PlaceOrderItemCommand.java
│   ├── PlaceOrderUseCase.java
│   ├── PlaceOrderUseCaseImpl.java
│   └── PlacedOrderResult.java
└── domain
    ├── Order.java
    ├── OrderItem.java
    ├── OrderPolicy.java
    ├── OrderRepository.java
    └── OrderStatus.java
```

## Papel de cada tipo

### `OrderStatus`
Representa os estados do pedido neste estágio inicial.

### `OrderItem`
Representa um item do pedido com validações locais de integridade.

### `Order`
Representa o agregado principal do domínio `order`.

### `OrderRepository`
Porta de saída do domínio para persistência.

### `OrderPolicy`
Centraliza regras de validação para colocação de pedido.

### `PlaceOrderUseCase`
Porta de entrada da aplicação para criar um pedido.

### `PlaceOrderUseCaseImpl`
Orquestra a validação, a criação do agregado e a persistência.

## O que ainda não entra neste capítulo

Para manter o passo correto, este capítulo ainda não adiciona:

- controllers
- requests e responses HTTP
- persistência JDBC concreta
- queries de leitura
- integração com pagamento
- testes de integração

## Resultado esperado

Ao final deste capítulo, o projeto já terá uma primeira fatia útil e coerente:

- o módulo `order` deixa de ser apenas estrutura
- o domínio começa a ganhar forma real
- a aplicação passa a ter um caso de uso concreto
- a persistência continua desacoplada por porta

## Próximo passo natural

No próximo capítulo, a tendência é introduzir a camada `api` para traduzir HTTP em comando de aplicação, mantendo o domínio protegido.
