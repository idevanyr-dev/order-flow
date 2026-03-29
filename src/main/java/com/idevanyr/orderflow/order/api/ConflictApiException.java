package com.idevanyr.orderflow.order.api;

final class ConflictApiException extends OrderApiException {

    ConflictApiException(String message) {
        super(message);
    }
}
