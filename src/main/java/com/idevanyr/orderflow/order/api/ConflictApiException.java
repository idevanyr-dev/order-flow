package com.idevanyr.orderflow.order.api;

class ConflictApiException extends OrderApiException {

    ConflictApiException(String message) {
        super(409, message);
    }
}
