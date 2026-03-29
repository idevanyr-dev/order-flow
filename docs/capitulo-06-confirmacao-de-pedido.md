# Capítulo 6 — Confirmação de pedido

Neste capítulo, o módulo `order` deixa de ter apenas criação de pedido e passa a suportar uma primeira transição real de estado: **confirmação**.

---

## Objetivo do capítulo

Adicionar o fluxo de confirmação de pedido, cobrindo:

- regra de negócio para confirmar pedido
- resultado explícito da confirmação
- caso de uso de aplicação
- endpoint HTTP de confirmação
- testes do domínio, aplicação e API

## Regras tratadas neste capítulo

A confirmação de pedido segue estas regras iniciais:

- pedido inexistente retorna `NotFound`
- pedido sem itens não pode ser confirmado
- pedido cancelado não pode ser confirmado
- pedido já confirmado não pode ser confirmado
- pedido válido muda para `CONFIRMED`

## Decisões do capítulo

### 1. A transição de estado nasce no domínio

A decisão de aceitar ou rejeitar a confirmação fica em `Order`, não no controller e nem no repositório.

### 2. O resultado continua explícito

A confirmação não usa exceções como fluxo principal.

Ela retorna um resultado fechado, permitindo traduzir facilmente para HTTP:

- `Success`
- `NotFound`
- `Rejected`

### 3. O controller continua fino

A camada `api` apenas:

- recebe o `orderId`
- chama o caso de uso
- traduz o resultado em resposta HTTP

## Estrutura adicionada neste capítulo

```text
src/main/java/com/idevanyr/orderflow/order
├── api
│   └── ConfirmOrderController.java
├── application
│   ├── ConfirmOrderCommand.java
│   ├── ConfirmOrderResult.java
│   ├── ConfirmOrderUseCase.java
│   └── ConfirmOrderUseCaseImpl.java
└── domain
    └── OrderConfirmation.java
```

## Endpoint adicionado

### Confirmar pedido

- **Método:** `POST`
- **Caminho:** `/orders/{orderId}/confirmation`

## Mapeamento HTTP inicial

- `Success` → `204 No Content`
- `NotFound` → `404 Not Found`
- `Rejected` → `422 Unprocessable Entity`

## O que ainda não entra neste capítulo

Para manter o foco, este capítulo ainda não adiciona:

- pagamento
- cancelamento
- histórico de transições
- versionamento otimista
- testes de integração com banco

## Resultado esperado

Ao final deste capítulo, o módulo `order` deixa de ser apenas criável e passa a suportar uma transição concreta de estado com regra de negócio explícita.

## Próximo passo natural

Depois da confirmação, os próximos caminhos mais fortes são:

- cancelamento de pedido
- consulta de pedido
- pagamento
- testes de integração da persistência
