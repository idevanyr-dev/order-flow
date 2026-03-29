package com.idevanyr.orderflow.order.api;

import java.util.List;

final class BadRequestApiException extends OrderApiException {

    private final List<String> errors;

    BadRequestApiException(List<String> errors) {
        super("request validation failed");
        this.errors = List.copyOf(errors);
    }

    List<String> errors() {
        return errors;
    }
}
