package com.idevanyr.orderflow.order.infrastructure.jdbc;

import com.idevanyr.orderflow.order.domain.Order;
import com.idevanyr.orderflow.order.domain.OrderItem;
import com.idevanyr.orderflow.order.domain.OrderRepository;
import com.idevanyr.orderflow.order.domain.OrderStatus;
import com.idevanyr.orderflow.support.PostgresIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(loaded).isPresent();
        assertThat(loaded.get().customerId()).isEqualTo("C-100");
        assertThat(loaded.get().status()).isEqualTo(OrderStatus.CONFIRMED);
        assertThat(loaded.get().items()).hasSize(2);
        assertThat(loaded.get().total()).isEqualByComparingTo("119.70");
    }
}
