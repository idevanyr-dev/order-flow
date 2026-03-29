package com.idevanyr.orderflow.order.api;

abstract class OrderApiException extends RuntimeException {

    private final int status;

    OrderApiException(int status, String message) {
        super(message);
        this.status = status;
    }

    int status() {
        return status;
    }
}
