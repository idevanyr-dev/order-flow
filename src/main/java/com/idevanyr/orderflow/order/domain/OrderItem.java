package com.idevanyr.orderflow.order.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record OrderItem(
        String productCode,
        int quantity,
        BigDecimal unitPrice
) {
    public OrderItem {
        Objects.requireNonNull(productCode, "productCode cannot be null");
        Objects.requireNonNull(unitPrice, "unitPrice cannot be null");

        if (productCode.isBlank()) {
            throw new IllegalArgumentException("productCode cannot be blank");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than zero");
        }

        if (unitPrice.signum() < 0) {
            throw new IllegalArgumentException("unitPrice must be non-negative");
        }
    }

    public BigDecimal lineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
