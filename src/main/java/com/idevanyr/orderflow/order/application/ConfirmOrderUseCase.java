package com.idevanyr.orderflow.order.application;

public interface ConfirmOrderUseCase {
    ConfirmOrderResult execute(ConfirmOrderCommand command);
}
