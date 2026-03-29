package com.idevanyr.orderflow.order.api;

import com.idevanyr.orderflow.order.application.PlaceOrderCommand;

import java.util.List;

public record PlaceOrderRequest(
        String customerId,
        List<PlaceOrderItemRequest> items
) {
    PlaceOrderCommand toCommand() {
        var commandItems = items == null
                ? List.<com.idevanyr.orderflow.order.application.PlaceOrderItemCommand>of()
                : items.stream().map(PlaceOrderItemRequest::toCommand).toList();

        return new PlaceOrderCommand(customerId, commandItems);
    }
}
