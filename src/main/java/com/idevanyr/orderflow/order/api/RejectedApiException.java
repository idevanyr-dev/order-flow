package com.idevanyr.orderflow.order.api;

final class RejectedApiException extends OrderApiException {

    RejectedApiException(String reason) {
        super(reason);
    }
}
