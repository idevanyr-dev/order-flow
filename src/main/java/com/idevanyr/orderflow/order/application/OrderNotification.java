package com.idevanyr.orderflow.order.application;

import java.math.BigDecimal;

public record OrderNotification(
        Type type,
        Long orderId,
        String customerId,
        BigDecimal totalAmount
) {
    public enum Type {
        ORDER_PLACED,
        ORDER_CONFIRMED,
        ORDER_CANCELLED,
        ORDER_PAID
    }
}
