package com.idevanyr.orderflow.order.application;

import java.math.BigDecimal;

public record OrderItemView(
        String productCode,
        int quantity,
        BigDecimal unitPrice
) {}
