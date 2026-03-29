package com.idevanyr.orderflow.order.domain;

public sealed interface OrderPayment permits OrderPayment.Success, OrderPayment.Rejected {

    record Success(Order order) implements OrderPayment {}

    record Rejected(String reason) implements OrderPayment {}
}
