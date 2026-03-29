package com.idevanyr.orderflow.order.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class OrderCancellationTest {

    @Test
    void shouldCancelEligibleOrder() {
        var order = new Order(
                1L,
                "C-100",
                List.of(new OrderItem("P-10", 2, new BigDecimal("49.90"))),
                OrderStatus.DRAFT
        );

        var result = order.cancel();

        var success = assertInstanceOf(OrderCancellation.Success.class, result);
        assertEquals(OrderStatus.CANCELLED, success.order().status());
    }

    @Test
    void shouldRejectPaidOrder() {
        var order = new Order(
                1L,
                "C-100",
                List.of(new OrderItem("P-10", 2, new BigDecimal("49.90"))),
                OrderStatus.PAID
        );

        var result = order.cancel();

        var rejected = assertInstanceOf(OrderCancellation.Rejected.class, result);
        assertEquals("paid order cannot be cancelled", rejected.reason());
    }

    @Test
    void shouldRejectAlreadyCancelledOrder() {
        var order = new Order(
                1L,
                "C-100",
                List.of(new OrderItem("P-10", 2, new BigDecimal("49.90"))),
                OrderStatus.CANCELLED
        );

        var result = order.cancel();

        var rejected = assertInstanceOf(OrderCancellation.Rejected.class, result);
        assertEquals("order is already cancelled", rejected.reason());
    }
}
