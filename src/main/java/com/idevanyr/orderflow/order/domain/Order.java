package com.idevanyr.orderflow.order.domain;

import com.idevanyr.orderflow.order.application.PlaceOrderCommand;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public final class Order {

    private final Long id;
    private final Long version;
    private final String customerId;
    private final List<OrderItem> items;
    private final OrderStatus status;

    public Order(Long id, String customerId, List<OrderItem> items, OrderStatus status) {
        this(id, null, customerId, items, status);
    }

    public Order(Long id, Long version, String customerId, List<OrderItem> items, OrderStatus status) {
        this.id = id;
        this.version = version;
        this.customerId = Objects.requireNonNull(customerId, "customerId cannot be null");
        this.items = List.copyOf(items);
        this.status = Objects.requireNonNull(status, "status cannot be null");
    }

    public static Order place(PlaceOrderCommand command) {
        var items = command.items().stream()
                .map(item -> new OrderItem(item.productCode(), item.quantity(), item.unitPrice()))
                .toList();

        return new Order(
                null,
                null,
                command.customerId(),
                items,
                OrderStatus.DRAFT
        );
    }

    public OrderConfirmation confirm() {
        if (items.isEmpty()) {
            return new OrderConfirmation.Rejected("order without items cannot be confirmed");
        }

        if (status == OrderStatus.CANCELLED) {
            return new OrderConfirmation.Rejected("cancelled order cannot be confirmed");
        }

        if (status == OrderStatus.CONFIRMED) {
            return new OrderConfirmation.Rejected("order is already confirmed");
        }

        if (status == OrderStatus.PAID) {
            return new OrderConfirmation.Rejected("paid order does not require confirmation");
        }

        return new OrderConfirmation.Success(
                new Order(id, version, customerId, items, OrderStatus.CONFIRMED)
        );
    }

    public OrderCancellation cancel() {
        if (status == OrderStatus.PAID) {
            return new OrderCancellation.Rejected("paid order cannot be cancelled");
        }

        if (status == OrderStatus.CANCELLED) {
            return new OrderCancellation.Rejected("order is already cancelled");
        }

        return new OrderCancellation.Success(
                new Order(id, version, customerId, items, OrderStatus.CANCELLED)
        );
    }

    public OrderPayment pay() {
        if (status == OrderStatus.CANCELLED) {
            return new OrderPayment.Rejected("cancelled order cannot be paid");
        }

        if (status == OrderStatus.DRAFT) {
            return new OrderPayment.Rejected("order must be confirmed before payment");
        }

        if (status == OrderStatus.PAID) {
            return new OrderPayment.Rejected("order is already paid");
        }

        return new OrderPayment.Success(
                new Order(id, version, customerId, items, OrderStatus.PAID)
        );
    }

    public BigDecimal total() {
        return items.stream()
                .map(OrderItem::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Long id() {
        return id;
    }

    public String customerId() {
        return customerId;
    }

    public Long version() {
        return version;
    }

    public List<OrderItem> items() {
        return items;
    }

    public OrderStatus status() {
        return status;
    }
}
