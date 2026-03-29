package com.idevanyr.orderflow.order.api;

final class NotFoundApiException extends OrderApiException {

    NotFoundApiException(String message) {
        super(message);
    }
}
