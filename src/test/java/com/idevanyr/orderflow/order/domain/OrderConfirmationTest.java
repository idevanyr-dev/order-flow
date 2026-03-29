package com.idevanyr.orderflow.order.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class OrderConfirmationTest {

    @Test
    void shouldConfirmDraftOrderWithItems() {
        var order = new Order(
                1L,
                "C-100",
                List.of(new OrderItem("P-10", 2, new BigDecimal("49.90"))),
                OrderStatus.DRAFT
        );

        var result = order.confirm();

        var success = assertInstanceOf(OrderConfirmation.Success.class, result);
        assertEquals(OrderStatus.CONFIRMED, success.order().status());
    }

    @Test
    void shouldRejectAlreadyConfirmedOrder() {
        var order = new Order(
                1L,
                "C-100",
                List.of(new OrderItem("P-10", 2, new BigDecimal("49.90"))),
                OrderStatus.CONFIRMED
        );

        var result = order.confirm();

        var rejected = assertInstanceOf(OrderConfirmation.Rejected.class, result);
        assertEquals("order is already confirmed", rejected.reason());
    }

    @Test
    void shouldRejectCancelledOrder() {
        var order = new Order(
                1L,
                "C-100",
                List.of(new OrderItem("P-10", 2, new BigDecimal("49.90"))),
                OrderStatus.CANCELLED
        );

        var result = order.confirm();

        var rejected = assertInstanceOf(OrderConfirmation.Rejected.class, result);
        assertEquals("cancelled order cannot be confirmed", rejected.reason());
    }
}
