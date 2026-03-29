package com.idevanyr.orderflow.order.infrastructure.payment;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
interface PaymentProviderHttpClient {

    @PostExchange("/payments/authorizations")
    PaymentProviderAuthorizeResponse authorize(@RequestBody PaymentProviderAuthorizeRequest request);
}
