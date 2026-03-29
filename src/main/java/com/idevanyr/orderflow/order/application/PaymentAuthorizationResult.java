package com.idevanyr.orderflow.order.application;

public sealed interface PaymentAuthorizationResult
        permits PaymentAuthorizationResult.Authorized,
        PaymentAuthorizationResult.Rejected,
        PaymentAuthorizationResult.Failed {

    record Authorized() implements PaymentAuthorizationResult {}

    record Rejected(String reason) implements PaymentAuthorizationResult {}

    record Failed(String reason) implements PaymentAuthorizationResult {}
}
