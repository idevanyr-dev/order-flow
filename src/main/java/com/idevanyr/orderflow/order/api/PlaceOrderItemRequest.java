package com.idevanyr.orderflow.order.api;

import com.idevanyr.orderflow.order.application.PlaceOrderItemCommand;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PlaceOrderItemRequest(
        @NotBlank String productCode,
        @Positive int quantity,
        @NotNull @DecimalMin(value = "0.01") BigDecimal unitPrice
) {
    PlaceOrderItemCommand toCommand() {
        return new PlaceOrderItemCommand(productCode, quantity, unitPrice);
    }
}
