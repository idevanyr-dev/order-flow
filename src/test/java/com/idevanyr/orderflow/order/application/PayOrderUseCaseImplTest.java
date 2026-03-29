package com.idevanyr.orderflow.order.application;

import com.idevanyr.orderflow.order.domain.Order;
import com.idevanyr.orderflow.order.domain.OrderItem;
import com.idevanyr.orderflow.order.domain.OrderRepository;
import com.idevanyr.orderflow.order.domain.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.dao.OptimisticLockingFailureException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PayOrderUseCaseImplTest {

    @Test
    void shouldReturnNotFoundWhenOrderDoesNotExist() {
        var repository = mock(OrderRepository.class);
        var paymentGateway = mock(PaymentGateway.class);
        var notificationGateway = mock(NotificationGateway.class);
        var useCase = new PayOrderUseCaseImpl(repository, paymentGateway, notificationGateway);

        when(repository.findById(1L)).thenReturn(Optional.empty());

        var result = useCase.execute(new PayOrderCommand(1L));

        assertInstanceOf(PayOrderResult.NotFound.class, result);
        verify(repository, never()).save(any());
        verify(paymentGateway, never()).authorize(any());
        verify(notificationGateway, never()).notify(any());
    }

    @Test
    void shouldPayConfirmedOrder() {
        var repository = mock(OrderRepository.class);
        var paymentGateway = mock(PaymentGateway.class);
        var notificationGateway = mock(NotificationGateway.class);
        var useCase = new PayOrderUseCaseImpl(repository, paymentGateway, notificationGateway);

        var order = new Order(
                1L,
                "C-100",
                List.of(new OrderItem("P-10", 2, new BigDecimal("49.90"))),
                OrderStatus.CONFIRMED
        );

        when(repository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentGateway.authorize(any()))
                .thenReturn(new PaymentAuthorizationResult.Authorized());
        when(repository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0, Order.class));

        var result = useCase.execute(new PayOrderCommand(1L));

        assertInstanceOf(PayOrderResult.Success.class, result);
        verify(paymentGateway).authorize(argThat(request ->
                request.orderId().equals(1L)
                        && request.customerId().equals("C-100")
                        && request.amount().compareTo(new BigDecimal("99.80")) == 0
        ));
        verify(repository).save(any(Order.class));
        verify(notificationGateway).notify(argThat(notification ->
                notification.type() == OrderNotification.Type.ORDER_PAID
                        && notification.orderId().equals(1L)
                        && notification.customerId().equals("C-100")
                        && notification.totalAmount().compareTo(new BigDecimal("99.80")) == 0
        ));
    }

    @Test
    void shouldRejectCancelledOrder() {
        var repository = mock(OrderRepository.class);
        var paymentGateway = mock(PaymentGateway.class);
        var notificationGateway = mock(NotificationGateway.class);
        var useCase = new PayOrderUseCaseImpl(repository, paymentGateway, notificationGateway);

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
        verify(paymentGateway, never()).authorize(any());
        verify(notificationGateway, never()).notify(any());
    }

    @Test
    void shouldReturnRejectedWhenGatewayRejectsAuthorization() {
        var repository = mock(OrderRepository.class);
        var paymentGateway = mock(PaymentGateway.class);
        var notificationGateway = mock(NotificationGateway.class);
        var useCase = new PayOrderUseCaseImpl(repository, paymentGateway, notificationGateway);

        var order = new Order(
                1L,
                "C-100",
                List.of(new OrderItem("P-10", 2, new BigDecimal("49.90"))),
                OrderStatus.CONFIRMED
        );

        when(repository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentGateway.authorize(any()))
                .thenReturn(new PaymentAuthorizationResult.Rejected("payment was declined"));

        var result = useCase.execute(new PayOrderCommand(1L));

        assertInstanceOf(PayOrderResult.Rejected.class, result);
        verify(repository, never()).save(any());
        verify(notificationGateway, never()).notify(any());
    }

    @Test
    void shouldReturnFailedWhenGatewayIsUnavailable() {
        var repository = mock(OrderRepository.class);
        var paymentGateway = mock(PaymentGateway.class);
        var notificationGateway = mock(NotificationGateway.class);
        var useCase = new PayOrderUseCaseImpl(repository, paymentGateway, notificationGateway);

        var order = new Order(
                1L,
                "C-100",
                List.of(new OrderItem("P-10", 2, new BigDecimal("49.90"))),
                OrderStatus.CONFIRMED
        );

        when(repository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentGateway.authorize(any()))
                .thenReturn(new PaymentAuthorizationResult.Failed("payment service is unavailable"));

        var result = useCase.execute(new PayOrderCommand(1L));

        assertInstanceOf(PayOrderResult.Failed.class, result);
        verify(repository, never()).save(any());
        verify(notificationGateway, never()).notify(any());
    }

    @Test
    void shouldReturnConflictWhenOrderWasChangedConcurrently() {
        var repository = mock(OrderRepository.class);
        var paymentGateway = mock(PaymentGateway.class);
        var notificationGateway = mock(NotificationGateway.class);
        var useCase = new PayOrderUseCaseImpl(repository, paymentGateway, notificationGateway);

        var order = new Order(
                1L,
                0L,
                "C-100",
                List.of(new OrderItem("P-10", 2, new BigDecimal("49.90"))),
                OrderStatus.CONFIRMED
        );

        when(repository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentGateway.authorize(any()))
                .thenReturn(new PaymentAuthorizationResult.Authorized());
        when(repository.save(any(Order.class))).thenThrow(new OptimisticLockingFailureException("stale order"));

        var result = useCase.execute(new PayOrderCommand(1L));

        assertInstanceOf(PayOrderResult.Conflict.class, result);
        verify(notificationGateway, never()).notify(any());
    }
}
