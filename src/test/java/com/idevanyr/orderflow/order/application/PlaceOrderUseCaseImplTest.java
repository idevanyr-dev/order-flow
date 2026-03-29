package com.idevanyr.orderflow.order.application;

import com.idevanyr.orderflow.order.domain.Order;
import com.idevanyr.orderflow.order.domain.OrderPolicy;
import com.idevanyr.orderflow.order.domain.OrderRepository;
import com.idevanyr.orderflow.order.domain.OrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PlaceOrderUseCaseImplTest {

    @Test
    void shouldReturnValidationErrorWhenCommandIsInvalid() {
        var policy = new OrderPolicy();
        var repository = mock(OrderRepository.class);
        var notificationGateway = mock(NotificationGateway.class);
        var useCase = new PlaceOrderUseCaseImpl(policy, repository, notificationGateway);
        var command = new PlaceOrderCommand("C-100", List.of());

        var result = useCase.execute(command);

        assertInstanceOf(PlacedOrderResult.ValidationError.class, result);
        verify(repository, never()).save(any());
        verify(notificationGateway, never()).notify(any());
    }

    @Test
    void shouldPersistOrderAndReturnSuccessWhenCommandIsValid() {
        var policy = new OrderPolicy();
        var repository = mock(OrderRepository.class);
        var notificationGateway = mock(NotificationGateway.class);
        var useCase = new PlaceOrderUseCaseImpl(policy, repository, notificationGateway);

        var command = new PlaceOrderCommand(
                "C-100",
                List.of(new PlaceOrderItemCommand("P-10", 2, new BigDecimal("49.90")))
        );

        when(repository.save(any(Order.class))).thenAnswer(invocation -> {
            var order = invocation.getArgument(0, Order.class);
            return new Order(1L, order.customerId(), order.items(), OrderStatus.DRAFT);
        });

        var result = useCase.execute(command);

        var success = assertInstanceOf(PlacedOrderResult.Success.class, result);
        assertEquals(1L, success.orderId());
        verify(repository).save(any(Order.class));
        verify(notificationGateway).notify(argThat(notification ->
                notification.type() == OrderNotification.Type.ORDER_PLACED
                        && notification.orderId().equals(1L)
                        && notification.customerId().equals("C-100")
                        && notification.totalAmount().compareTo(new BigDecimal("99.80")) == 0
        ));
    }
}
