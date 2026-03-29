package com.idevanyr.orderflow.order.application;

public interface PaymentGateway {
    PaymentAuthorizationResult authorize(PaymentAuthorizationRequest request);
}
