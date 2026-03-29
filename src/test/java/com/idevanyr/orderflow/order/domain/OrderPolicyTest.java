package com.idevanyr.orderflow.order.domain;

import com.idevanyr.orderflow.order.application.PlaceOrderCommand;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderPolicyTest {

    @Test
    void shouldRejectOrderWithoutItems() {
        var policy = new OrderPolicy();
        var command = new PlaceOrderCommand("C-100", List.of());

        var errors = policy.validateForPlacement(command);

        assertTrue(errors.contains("order must contain at least one item"));
    }
}
