package com.idevanyr.orderflow.order.infrastructure.jdbc;

import com.idevanyr.orderflow.order.application.FindOrderDetailsQuery;
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
class JdbcFindOrderDetailsQueryIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private FindOrderDetailsQuery findOrderDetailsQuery;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void cleanDatabase() {
        jdbcClient.sql("delete from order_items").update();
        jdbcClient.sql("delete from orders").update();
    }

    @Test
    void shouldReturnOrderDetailsViewFromRealDatabase() {
        var saved = orderRepository.save(new Order(
                null,
                "C-100",
                List.of(
                        new OrderItem("P-10", 2, new BigDecimal("49.90")),
                        new OrderItem("P-20", 1, new BigDecimal("19.90"))
                ),
                OrderStatus.PAID
        ));

        var result = findOrderDetailsQuery.execute(saved.id());

        assertThat(result).isPresent();
        assertThat(result.get().orderId()).isEqualTo(saved.id());
        assertThat(result.get().customerId()).isEqualTo("C-100");
        assertThat(result.get().status()).isEqualTo("PAID");
        assertThat(result.get().totalAmount()).isEqualByComparingTo("119.70");
        assertThat(result.get().totalItemsQuantity()).isEqualTo(3);
        assertThat(result.get().items()).hasSize(2);
    }
}
