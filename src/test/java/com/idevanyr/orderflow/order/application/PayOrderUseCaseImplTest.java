package com.idevanyr.orderflow.order.application;

import com.idevanyr.orderflow.order.domain.Order;
import com.idevanyr.orderflow.order.domain.OrderItem;
import com.idevanyr.orderflow.order.domain.OrderRepository;
import com.idevanyr.orderflow.order.domain.OrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PayOrderUseCaseImplTest {

    @Test
    void shouldReturnNotFoundWhenOrderDoesNotExist() {
        var repository = mock(OrderRepository.class);
        var useCase = new PayOrderUseCaseImpl(repository);

        when(repository.findById(1L)).thenReturn(Optional.empty());

        var result = useCase.execute(new PayOrderCommand(1L));

        assertInstanceOf(PayOrderResult.NotFound.class, result);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldPayConfirmedOrder() {
        var repository = mock(OrderRepository.class);
        var useCase = new PayOrderUseCaseImpl(repository);

        var order = new Order(
                1L,
                "C-100",
                List.of(new OrderItem("P-10", 2, new BigDecimal("49.90"))),
                OrderStatus.CONFIRMED
        );

        when(repository.findById(1L)).thenReturn(Optional.of(order));
        when(repository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0, Order.class));

        var result = useCase.execute(new PayOrderCommand(1L));

        assertInstanceOf(PayOrderResult.Success.class, result);
        verify(repository).save(any(Order.class));
    }

    @Test
    void shouldRejectCancelledOrder() {
        var repository = mock(OrderRepository.class);
        var useCase = new PayOrderUseCaseImpl(repository);

        var order = new Order(
                1L,
                "C-100",
                List.of(new OrderItem("P-10", 2, new BigDecimal("49.90"))),
                OrderStatus.CANCELLED
        );

        when(repository.findById(1L)).thenReturn(Optional.of(order));

        var result = useCase.execute(new PayOrderCommand(1L));

        assertInstanceOf(PayOrderResult.Rejected.class, result);
        verify(repository, never()).save(any());
    }
}
