package com.idevanyr.orderflow.order.application;

public sealed interface CancelOrderResult
        permits CancelOrderResult.Success, CancelOrderResult.NotFound, CancelOrderResult.Rejected {

    record Success() implements CancelOrderResult {}

    record NotFound() implements CancelOrderResult {}

    record Rejected(String reason) implements CancelOrderResult {}
}
