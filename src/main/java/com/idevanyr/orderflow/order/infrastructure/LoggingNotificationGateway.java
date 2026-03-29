package com.idevanyr.orderflow.order.infrastructure;

import com.idevanyr.orderflow.order.application.NotificationGateway;
import com.idevanyr.orderflow.order.application.OrderNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class LoggingNotificationGateway implements NotificationGateway {

    private static final Logger log = LoggerFactory.getLogger(LoggingNotificationGateway.class);

    @Override
    public void notify(OrderNotification notification) {
        log.info(
                "Dispatching notification type={} for orderId={}, customerId={}, totalAmount={}",
                notification.type(),
                notification.orderId(),
                notification.customerId(),
                notification.totalAmount()
        );
    }
}
