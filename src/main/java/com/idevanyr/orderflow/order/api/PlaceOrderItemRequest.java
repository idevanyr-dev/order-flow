package com.idevanyr.orderflow.order.api;

import com.idevanyr.orderflow.order.application.PlaceOrderItemCommand;

import java.math.BigDecimal;

public record PlaceOrderItemRequest(
        String productCode,
        int quantity,
        BigDecimal unitPrice
) {
    PlaceOrderItemCommand toCommand() {
        return new PlaceOrderItemCommand(productCode, quantity, unitPrice);
    }
}
