package com.idevanyr.orderflow.order.infrastructure.payment;

import com.idevanyr.orderflow.order.application.PaymentAuthorizationRequest;
import com.idevanyr.orderflow.order.application.PaymentAuthorizationResult;
import com.idevanyr.orderflow.order.application.PaymentGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.util.Locale;

@Component
class HttpPaymentGateway implements PaymentGateway {

    private final PaymentProviderHttpClient paymentProviderHttpClient;

    HttpPaymentGateway(PaymentProviderHttpClient paymentProviderHttpClient) {
        this.paymentProviderHttpClient = paymentProviderHttpClient;
    }

    @Override
    public PaymentAuthorizationResult authorize(PaymentAuthorizationRequest request) {
        try {
            var response = paymentProviderHttpClient.authorize(new PaymentProviderAuthorizeRequest(
                    request.orderId(),
                    request.customerId(),
                    request.amount()
            ));

            if (response == null || response.status() == null) {
                return new PaymentAuthorizationResult.Failed("payment provider returned an invalid response");
            }

            return switch (response.status().toUpperCase(Locale.ROOT)) {
                case "AUTHORIZED" -> new PaymentAuthorizationResult.Authorized();
                case "REJECTED" -> new PaymentAuthorizationResult.Rejected(
                        response.reason() == null || response.reason().isBlank()
                                ? "payment was declined"
                                : response.reason()
                );
                default -> new PaymentAuthorizationResult.Failed("payment provider returned an unknown status");
            };
        } catch (HttpStatusCodeException exception) {
            if (exception.getStatusCode().is4xxClientError()) {
                return new PaymentAuthorizationResult.Rejected("payment was declined");
            }

            return new PaymentAuthorizationResult.Failed("payment provider is unavailable");
        } catch (RestClientException _) {
            return new PaymentAuthorizationResult.Failed("payment provider is unavailable");
        }
    }
}
