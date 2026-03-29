package com.idevanyr.orderflow.order.api;

import com.idevanyr.orderflow.order.application.PlaceOrderCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record PlaceOrderRequest(
        @NotBlank String customerId,
        @NotEmpty List<@Valid PlaceOrderItemRequest> items
) {
    PlaceOrderCommand toCommand() {
        var commandItems = items == null
                ? List.<com.idevanyr.orderflow.order.application.PlaceOrderItemCommand>of()
                : items.stream().map(PlaceOrderItemRequest::toCommand).toList();

        return new PlaceOrderCommand(customerId, commandItems);
    }
}
