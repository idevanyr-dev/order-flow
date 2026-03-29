package com.idevanyr.orderflow.order.infrastructure.payment;

import com.idevanyr.orderflow.order.application.PaymentAuthorizationRequest;
import com.idevanyr.orderflow.order.application.PaymentAuthorizationResult;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HttpPaymentGatewayTest {

    @Test
    void shouldAuthorizeWhenProviderReturnsAuthorized() {
        var client = mock(PaymentProviderHttpClient.class);
        var gateway = new HttpPaymentGateway(client);

        when(client.authorize(any()))
                .thenReturn(new PaymentProviderAuthorizeResponse("AUTHORIZED", null));

        var result = gateway.authorize(new PaymentAuthorizationRequest(1L, "C-100", new BigDecimal("99.80")));

        assertInstanceOf(PaymentAuthorizationResult.Authorized.class, result);
    }

    @Test
    void shouldRejectWhenProviderReturnsRejected() {
        var client = mock(PaymentProviderHttpClient.class);
        var gateway = new HttpPaymentGateway(client);

        when(client.authorize(any()))
                .thenReturn(new PaymentProviderAuthorizeResponse("REJECTED", "card limit exceeded"));

        var result = gateway.authorize(new PaymentAuthorizationRequest(1L, "C-100", new BigDecimal("99.80")));

        var rejected = assertInstanceOf(PaymentAuthorizationResult.Rejected.class, result);
        org.junit.jupiter.api.Assertions.assertEquals("card limit exceeded", rejected.reason());
    }

    @Test
    void shouldFailWhenProviderReturnsUnknownStatus() {
        var client = mock(PaymentProviderHttpClient.class);
        var gateway = new HttpPaymentGateway(client);

        when(client.authorize(any()))
                .thenReturn(new PaymentProviderAuthorizeResponse("PENDING", null));

        var result = gateway.authorize(new PaymentAuthorizationRequest(1L, "C-100", new BigDecimal("99.80")));

        assertInstanceOf(PaymentAuthorizationResult.Failed.class, result);
    }

    @Test
    void shouldRejectWhenProviderRespondsWithClientError() {
        var client = mock(PaymentProviderHttpClient.class);
        var gateway = new HttpPaymentGateway(client);

        when(client.authorize(any()))
                .thenThrow(HttpClientErrorException.create(
                        HttpStatus.valueOf(422),
                        "Rejected",
                        HttpHeaders.EMPTY,
                        new byte[0],
                        StandardCharsets.UTF_8
                ));

        var result = gateway.authorize(new PaymentAuthorizationRequest(1L, "C-100", new BigDecimal("99.80")));

        assertInstanceOf(PaymentAuthorizationResult.Rejected.class, result);
    }

    @Test
    void shouldFailWhenProviderIsUnavailable() {
        var client = mock(PaymentProviderHttpClient.class);
        var gateway = new HttpPaymentGateway(client);

        when(client.authorize(any()))
                .thenThrow(new ResourceAccessException("Connection refused"));

        var result = gateway.authorize(new PaymentAuthorizationRequest(1L, "C-100", new BigDecimal("99.80")));

        assertInstanceOf(PaymentAuthorizationResult.Failed.class, result);
    }
}
