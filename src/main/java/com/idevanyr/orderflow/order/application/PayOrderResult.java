package com.idevanyr.orderflow.order.application;

public sealed interface PayOrderResult
        permits PayOrderResult.Success,
        PayOrderResult.NotFound,
        PayOrderResult.Rejected,
        PayOrderResult.Conflict,
        PayOrderResult.Failed {

    record Success() implements PayOrderResult {}

    record NotFound() implements PayOrderResult {}

    record Rejected(String reason) implements PayOrderResult {}

    record Conflict(String reason) implements PayOrderResult {}

    record Failed(String reason) implements PayOrderResult {}
}
