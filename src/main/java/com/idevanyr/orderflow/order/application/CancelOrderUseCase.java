package com.idevanyr.orderflow.order.application;

public interface CancelOrderUseCase {
    CancelOrderResult execute(CancelOrderCommand command);
}
