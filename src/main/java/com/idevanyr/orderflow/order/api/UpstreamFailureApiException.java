package com.idevanyr.orderflow.order.api;

class UpstreamFailureApiException extends OrderApiException {

    UpstreamFailureApiException(String reason) {
        super(502, reason);
    }
}
