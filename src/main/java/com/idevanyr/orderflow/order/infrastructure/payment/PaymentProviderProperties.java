package com.idevanyr.orderflow.order.infrastructure.payment;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "order.payment.provider")
record PaymentProviderProperties(
        String baseUrl,
        String apiKey
) {}
