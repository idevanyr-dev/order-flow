package com.idevanyr.orderflow.order.application;

public sealed interface ConfirmOrderResult
        permits ConfirmOrderResult.Success, ConfirmOrderResult.NotFound, ConfirmOrderResult.Rejected {

    record Success() implements ConfirmOrderResult {}

    record NotFound() implements ConfirmOrderResult {}

    record Rejected(String reason) implements ConfirmOrderResult {}
}
