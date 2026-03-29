package com.idevanyr.orderflow.order.domain;

public sealed interface OrderConfirmation permits OrderConfirmation.Success, OrderConfirmation.Rejected {

    record Success(Order order) implements OrderConfirmation {}

    record Rejected(String reason) implements OrderConfirmation {}
}
