# Capítulo 7 — Cancelamento de pedido

Neste capítulo, o módulo `order` passa a suportar uma segunda transição importante de estado: **cancelamento**.

---

## Objetivo do capítulo

Adicionar o fluxo de cancelamento de pedido, cobrindo:

- regra de negócio para cancelar pedido
- resultado explícito do cancelamento
- caso de uso de aplicação
- endpoint HTTP de cancelamento
- testes do domínio, aplicação e API

## Regras tratadas neste capítulo

O cancelamento de pedido segue estas regras iniciais:

- pedido inexistente retorna `NotFound`
- pedido pago não pode ser cancelado
- pedido já cancelado não pode ser cancelado
- pedido elegível muda para `CANCELLED`

## Decisões do capítulo

### 1. A transição de estado continua no domínio

A decisão de aceitar ou rejeitar o cancelamento fica em `Order`, por meio do método `cancel()`.

### 2. O resultado continua explícito

O cancelamento retorna um resultado fechado:

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
│   └── CancelOrderController.java
├── application
│   ├── CancelOrderCommand.java
│   ├── CancelOrderResult.java
│   ├── CancelOrderUseCase.java
│   └── CancelOrderUseCaseImpl.java
└── domain
    └── OrderCancellation.java
```

## Endpoint adicionado

### Cancelar pedido

- **Método:** `POST`
- **Caminho:** `/orders/{orderId}/cancellation`

## Mapeamento HTTP inicial

- `Success` → `204 No Content`
- `NotFound` → `404 Not Found`
- `Rejected` → `422 Unprocessable Entity`

## O que ainda não entra neste capítulo

Para manter o foco, este capítulo ainda não adiciona:

- reabertura de pedido
- histórico de transições
- versionamento otimista
- testes de integração com banco

## Resultado esperado

Ao final deste capítulo, o módulo `order` passa a suportar duas transições reais de estado com regras de negócio explícitas: confirmação e cancelamento.

## Próximo passo natural

Depois do cancelamento, os próximos caminhos mais fortes são:

- consulta de pedido
- pagamento
- query model separado para leitura
- testes de integração da persistência
