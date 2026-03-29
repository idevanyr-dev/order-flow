package com.idevanyr.orderflow.order.api;

final class UpstreamFailureApiException extends OrderApiException {

    UpstreamFailureApiException(String reason) {
        super(reason);
    }
}
