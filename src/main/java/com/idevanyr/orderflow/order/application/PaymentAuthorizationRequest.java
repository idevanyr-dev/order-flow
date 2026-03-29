package com.idevanyr.orderflow.order.application;

import java.math.BigDecimal;

public record PaymentAuthorizationRequest(
        Long orderId,
        String customerId,
        BigDecimal amount
) {}
