package com.idevanyr.orderflow.order.application;

public interface NotificationGateway {
    void notify(OrderNotification notification);
}
