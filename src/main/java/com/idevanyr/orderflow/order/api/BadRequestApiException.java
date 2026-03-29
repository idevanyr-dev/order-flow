package com.idevanyr.orderflow.order.api;

import java.util.List;

class BadRequestApiException extends OrderApiException {

    private final List<String> errors;

    BadRequestApiException(List<String> errors) {
        super(400, "request validation failed");
        this.errors = List.copyOf(errors);
    }

    List<String> errors() {
        return errors;
    }
}
