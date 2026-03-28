package com.idevanyr.orderflow.order.application;

public interface PlaceOrderUseCase {
    PlacedOrderResult execute(PlaceOrderCommand command);
}
