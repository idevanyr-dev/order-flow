package com.idevanyr.orderflow.order.application;

public sealed interface CancelOrderResult
        permits CancelOrderResult.Success,
        CancelOrderResult.NotFound,
        CancelOrderResult.Rejected,
        CancelOrderResult.Conflict {

    record Success() implements CancelOrderResult {}

    record NotFound() implements CancelOrderResult {}

    record Rejected(String reason) implements CancelOrderResult {}

    record Conflict(String reason) implements CancelOrderResult {}
}
