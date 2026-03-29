package com.idevanyr.orderflow.order.api;

class RejectedApiException extends OrderApiException {

    RejectedApiException(String reason) {
        super(422, reason);
    }
}
