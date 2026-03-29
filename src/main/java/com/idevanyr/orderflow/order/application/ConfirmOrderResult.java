package com.idevanyr.orderflow.order.application;

public sealed interface ConfirmOrderResult
        permits ConfirmOrderResult.Success,
        ConfirmOrderResult.NotFound,
        ConfirmOrderResult.Rejected,
        ConfirmOrderResult.Conflict {

    record Success() implements ConfirmOrderResult {}

    record NotFound() implements ConfirmOrderResult {}

    record Rejected(String reason) implements ConfirmOrderResult {}

    record Conflict(String reason) implements ConfirmOrderResult {}
}
