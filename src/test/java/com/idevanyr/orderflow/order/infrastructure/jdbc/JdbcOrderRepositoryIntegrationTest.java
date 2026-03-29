package com.idevanyr.orderflow.order.infrastructure.jdbc;

import com.idevanyr.orderflow.order.domain.Order;
import com.idevanyr.orderflow.order.domain.OrderItem;
import com.idevanyr.orderflow.order.domain.OrderRepository;
import com.idevanyr.orderflow.order.domain.OrderStatus;
import com.idevanyr.orderflow.support.PostgresIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@PostgresIntegrationTest
class JdbcOrderRepositoryIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void cleanDatabase() {
        jdbcClient.sql("delete from order_items").update();
        jdbcClient.sql("delete from orders").update();
    }

    @Test
    void shouldPersistAndLoadOrderAggregateWithItems() {
        var order = new Order(
                null,
                "C-100",
                List.of(
                        new OrderItem("P-10", 2, new BigDecimal("49.90")),
                        new OrderItem("P-20", 1, new BigDecimal("19.90"))
                ),
                OrderStatus.CONFIRMED
        );

        var saved = orderRepository.save(order);

        var loaded = orderRepository.findById(saved.id());

        assertThat(saved.id()).isNotNull();
        assertThat(saved.version()).isZero();
        assertThat(loaded).isPresent();
        assertThat(loaded.get().customerId()).isEqualTo("C-100");
        assertThat(loaded.get().status()).isEqualTo(OrderStatus.CONFIRMED);
        assertThat(loaded.get().version()).isZero();
        assertThat(loaded.get().items()).hasSize(2);
        assertThat(loaded.get().total()).isEqualByComparingTo("119.70");
    }

    @Test
    void shouldRejectStaleOrderUpdate() {
        var created = orderRepository.save(new Order(
                null,
                "C-100",
                List.of(new OrderItem("P-10", 2, new BigDecimal("49.90"))),
                OrderStatus.DRAFT
        ));

        var firstCopy = orderRepository.findById(created.id()).orElseThrow();
        var secondCopy = orderRepository.findById(created.id()).orElseThrow();

        var confirmedOrder = ((com.idevanyr.orderflow.order.domain.OrderConfirmation.Success) firstCopy.confirm()).order();
        var cancelledOrder = ((com.idevanyr.orderflow.order.domain.OrderCancellation.Success) secondCopy.cancel()).order();

        var updated = orderRepository.save(confirmedOrder);

        assertThat(updated.version()).isEqualTo(1L);
        assertThatThrownBy(() -> orderRepository.save(cancelledOrder))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }
}
