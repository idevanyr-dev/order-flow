package com.idevanyr.orderflow.order.application;

import com.idevanyr.orderflow.order.domain.Order;
import com.idevanyr.orderflow.order.domain.OrderItem;
import com.idevanyr.orderflow.order.domain.OrderRepository;
import com.idevanyr.orderflow.order.domain.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.dao.OptimisticLockingFailureException;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConfirmOrderUseCaseImplTest {

    @Test
    void shouldReturnNotFoundWhenOrderDoesNotExist() {
        var repository = mock(OrderRepository.class);
        var notificationGateway = mock(NotificationGateway.class);
        var useCase = new ConfirmOrderUseCaseImpl(repository, notificationGateway);

        when(repository.findById(1L)).thenReturn(java.util.Optional.empty());

        var result = useCase.execute(new ConfirmOrderCommand(1L));

        assertInstanceOf(ConfirmOrderResult.NotFound.class, result);
        verify(repository, never()).save(any());
        verify(notificationGateway, never()).notify(any());
    }

    @Test
    void shouldConfirmOrderWhenItIsValid() {
        var repository = mock(OrderRepository.class);
        var notificationGateway = mock(NotificationGateway.class);
        var useCase = new ConfirmOrderUseCaseImpl(repository, notificationGateway);

        var order = new Order(
                1L,
                "C-100",
                List.of(new OrderItem("P-10", 2, new BigDecimal("49.90"))),
                OrderStatus.DRAFT
        );

        when(repository.findById(1L)).thenReturn(java.util.Optional.of(order));
        when(repository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0, Order.class));

        var result = useCase.execute(new ConfirmOrderCommand(1L));

        assertInstanceOf(ConfirmOrderResult.Success.class, result);
        verify(repository).save(any(Order.class));
        verify(notificationGateway).notify(argThat(notification ->
                notification.type() == OrderNotification.Type.ORDER_CONFIRMED
                        && notification.orderId().equals(1L)
        ));
    }

    @Test
    void shouldRejectCancelledOrder() {
        var repository = mock(OrderRepository.class);
        var notificationGateway = mock(NotificationGateway.class);
        var useCase = new ConfirmOrderUseCaseImpl(repository, notificationGateway);

        var order = new Order(
                1L,
                "C-100",
                List.of(new OrderItem("P-10", 2, new BigDecimal("49.90"))),
                OrderStatus.CANCELLED
        );

        when(repository.findById(1L)).thenReturn(java.util.Optional.of(order));

        var result = useCase.execute(new ConfirmOrderCommand(1L));

        assertInstanceOf(ConfirmOrderResult.Rejected.class, result);
        verify(repository, never()).save(any());
        verify(notificationGateway, never()).notify(any());
    }

    @Test
    void shouldReturnConflictWhenOrderWasChangedConcurrently() {
        var repository = mock(OrderRepository.class);
        var notificationGateway = mock(NotificationGateway.class);
        var useCase = new ConfirmOrderUseCaseImpl(repository, notificationGateway);

        var order = new Order(
                1L,
                0L,
                "C-100",
                List.of(new OrderItem("P-10", 2, new BigDecimal("49.90"))),
                OrderStatus.DRAFT
        );

        when(repository.findById(1L)).thenReturn(java.util.Optional.of(order));
        when(repository.save(any(Order.class))).thenThrow(new OptimisticLockingFailureException("stale order"));

        var result = useCase.execute(new ConfirmOrderCommand(1L));

        assertInstanceOf(ConfirmOrderResult.Conflict.class, result);
        verify(notificationGateway, never()).notify(any());
    }
}
