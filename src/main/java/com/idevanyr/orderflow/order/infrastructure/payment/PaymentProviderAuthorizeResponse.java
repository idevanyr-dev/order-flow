package com.idevanyr.orderflow.order.infrastructure.payment;

record PaymentProviderAuthorizeResponse(
        String status,
        String reason
) {}
