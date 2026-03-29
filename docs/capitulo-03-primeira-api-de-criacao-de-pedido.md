# Capítulo 3 — Primeira API de criação de pedido

Neste capítulo, o módulo `order` ganha sua primeira entrada HTTP real.

O objetivo é expor um endpoint de criação de pedido sem contaminar o domínio com detalhes de transporte.

---

## Objetivo do capítulo

Adicionar a primeira camada `api` do módulo `order`, responsável por:

- receber requisições HTTP
- traduzir request para comando de aplicação
- invocar o caso de uso `PlaceOrderUseCase`
- converter o resultado em resposta HTTP adequada

## Decisões do capítulo

### 1. O controller não contém regra de negócio

O papel do controller é apenas:

- receber a requisição
- converter para comando
- chamar o caso de uso
- devolver o status HTTP correspondente

### 2. Request e response HTTP ficam isolados em `api`

Os tipos HTTP não devem vazar para `domain` ou `application`.

Por isso, este capítulo introduz:

- `PlaceOrderRequest`
- `PlaceOrderItemRequest`
- `PlaceOrderResponse`
- `ErrorResponse`

### 3. Resultado da aplicação é traduzido na borda

A tradução de `PlacedOrderResult` para HTTP acontece no controller.

Mapeamento inicial:

- `Success` → `201 Created`
- `ValidationError` → `400 Bad Request`

## Estrutura adicionada neste capítulo

```text
src/main/java/com/idevanyr/orderflow/order/api
├── ErrorResponse.java
├── PlaceOrderController.java
├── PlaceOrderItemRequest.java
├── PlaceOrderRequest.java
└── PlaceOrderResponse.java
```

## Endpoint adicionado

### Criar pedido

- **Método:** `POST`
- **Caminho:** `/orders`

### Exemplo de payload

```json
{
  "customerId": "C-100",
  "items": [
    {
      "productCode": "P-10",
      "quantity": 2,
      "unitPrice": 49.90
    }
  ]
}
```

### Exemplo de resposta de sucesso

```json
{
  "orderId": 123
}
```

### Exemplo de resposta de erro

```json
{
  "errors": [
    "order must contain at least one item"
  ]
}
```

## O que ainda não entra neste capítulo

Para manter o passo certo, este capítulo ainda não adiciona:

- validação com Bean Validation
- tratamento global de exceções
- persistência JDBC concreta
- endpoint de consulta
- confirmação de pedido

## Resultado esperado

Ao final deste capítulo, o projeto passa a ter:

- a primeira borda HTTP do módulo `order`
- tradução limpa entre API e aplicação
- uma base pronta para evoluir validação, persistência e leitura

## Próximo passo natural

O próximo capítulo pode seguir por um destes caminhos:

- persistência concreta com Spring Data JDBC
- testes do caso de uso e do controller
- validação HTTP com Bean Validation
