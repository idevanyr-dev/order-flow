# Capítulo 5 — Testes do módulo order

Neste capítulo, o projeto começa a consolidar uma base de segurança para evolução.

Até aqui, o módulo `order` já possui:

- domínio inicial
- caso de uso de criação de pedido
- API HTTP para `POST /orders`
- persistência concreta com Spring Data JDBC

Agora é hora de validar o comportamento com testes.

---

## Objetivo do capítulo

Adicionar os primeiros testes automatizados do módulo `order`, cobrindo:

- regras de validação da colocação de pedido
- comportamento do caso de uso `PlaceOrderUseCase`
- tradução de resultados para HTTP no controller

## Decisões do capítulo

### 1. Testar regras críticas cedo

As primeiras regras mais importantes deste módulo são:

- pedido sem item deve ser rejeitado
- pedido válido deve ser persistido
- erro de validação deve virar `400 Bad Request`
- sucesso deve virar `201 Created`

### 2. Testar cada camada no nível certo

Este capítulo separa as preocupações:

- `OrderPolicyTest`: valida regras de entrada
- `PlaceOrderUseCaseImplTest`: valida orquestração da aplicação
- `PlaceOrderControllerTest`: valida a borda HTTP

### 3. Evitar custo desnecessário de integração neste momento

Os testes deste capítulo não dependem de banco real.

A ideia aqui é consolidar comportamento e contratos antes de ampliar para testes integrados com persistência.

## Estrutura adicionada neste capítulo

```text
src/test/java/com/idevanyr/orderflow/order
├── api
│   └── PlaceOrderControllerTest.java
├── application
│   └── PlaceOrderUseCaseImplTest.java
└── domain
    └── OrderPolicyTest.java
```

## O que cada teste cobre

### `OrderPolicyTest`
Confirma que a política rejeita pedidos inválidos.

### `PlaceOrderUseCaseImplTest`
Confirma que:

- não persiste quando a validação falha
- persiste quando o pedido é válido
- retorna `Success` com identificador do pedido

### `PlaceOrderControllerTest`
Confirma que o endpoint `POST /orders`:

- retorna `201` quando o caso de uso responde com sucesso
- retorna `400` quando o caso de uso responde com erro de validação

## O que ainda não entra neste capítulo

Para manter o foco, este capítulo ainda não adiciona:

- testes de integração com PostgreSQL
- testes do repositório JDBC
- testes de migração
- cobertura de confirmação e cancelamento

## Resultado esperado

Ao final deste capítulo, o projeto passa a ter uma base mínima, mas muito importante, de segurança para refatoração.

Isso reduz o risco de quebrar regras de negócio e contratos HTTP à medida que os próximos capítulos ampliarem o sistema.

## Próximo passo natural

O próximo capítulo pode seguir por um destes caminhos:

- endpoint de consulta de pedido
- confirmação e cancelamento de pedido
- testes de integração da persistência
- validação HTTP com Bean Validation
