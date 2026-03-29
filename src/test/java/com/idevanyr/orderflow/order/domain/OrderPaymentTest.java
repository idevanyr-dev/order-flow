package com.idevanyr.orderflow.order.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class OrderPaymentTest {

    @Test
    void shouldPayConfirmedOrder() {
        var order = new Order(
                1L,
                "C-100",
                List.of(new OrderItem("P-10", 2, new BigDecimal("49.90"))),
                OrderStatus.CONFIRMED
        );

        var result = order.pay();

        var success = assertInstanceOf(OrderPayment.Success.class, result);
        assertEquals(OrderStatus.PAID, success.order().status());
    }

    @Test
    void shouldRejectDraftOrder() {
        var order = new Order(
                1L,
                "C-100",
                List.of(new OrderItem("P-10", 2, new BigDecimal("49.90"))),
                OrderStatus.DRAFT
        );

        var result = order.pay();

        var rejected = assertInstanceOf(OrderPayment.Rejected.class, result);
        assertEquals("order must be confirmed before payment", rejected.reason());
    }

    @Test
    void shouldRejectCancelledOrder() {
        var order = new Order(
                1L,
                "C-100",
                List.of(new OrderItem("P-10", 2, new BigDecimal("49.90"))),
                OrderStatus.CANCELLED
        );

        var result = order.pay();

        var rejected = assertInstanceOf(OrderPayment.Rejected.class, result);
        assertEquals("cancelled order cannot be paid", rejected.reason());
    }

    @Test
    void shouldRejectAlreadyPaidOrder() {
        var order = new Order(
                1L,
                "C-100",
                List.of(new OrderItem("P-10", 2, new BigDecimal("49.90"))),
                OrderStatus.PAID
        );

        var result = order.pay();

        var rejected = assertInstanceOf(OrderPayment.Rejected.class, result);
        assertEquals("order is already paid", rejected.reason());
    }
}
