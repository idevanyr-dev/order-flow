package com.idevanyr.orderflow.order.api;

abstract class OrderApiException extends RuntimeException {

    OrderApiException(String message) {
        super(message);
    }
}
