package com.idevanyr.orderflow.order.infrastructure.payment;

import java.math.BigDecimal;

record PaymentProviderAuthorizeRequest(
        Long orderId,
        String customerId,
        BigDecimal amount
) {}
