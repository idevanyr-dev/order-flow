package com.idevanyr.orderflow.order.api;

class NotFoundApiException extends OrderApiException {

    NotFoundApiException(String message) {
        super(404, message);
    }
}
