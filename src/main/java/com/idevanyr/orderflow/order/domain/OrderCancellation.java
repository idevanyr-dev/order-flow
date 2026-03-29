package com.idevanyr.orderflow.order.domain;

public sealed interface OrderCancellation permits OrderCancellation.Success, OrderCancellation.Rejected {

    record Success(Order order) implements OrderCancellation {}

    record Rejected(String reason) implements OrderCancellation {}
}
