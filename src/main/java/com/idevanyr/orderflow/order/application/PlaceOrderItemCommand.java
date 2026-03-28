package com.idevanyr.orderflow.order.application;

import java.math.BigDecimal;

public record PlaceOrderItemCommand(
        String productCode,
        int quantity,
        BigDecimal unitPrice
) {}
