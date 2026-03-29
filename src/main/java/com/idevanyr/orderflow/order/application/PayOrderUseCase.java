package com.idevanyr.orderflow.order.application;

public interface PayOrderUseCase {
    PayOrderResult execute(PayOrderCommand command);
}
