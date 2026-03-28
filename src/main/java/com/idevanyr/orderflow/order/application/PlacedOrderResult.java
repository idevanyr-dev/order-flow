package com.idevanyr.orderflow.order.application;

import java.util.List;

public sealed interface PlacedOrderResult
        permits PlacedOrderResult.Success, PlacedOrderResult.ValidationError {

    record Success(Long orderId) implements PlacedOrderResult {}

    record ValidationError(List<String> errors) implements PlacedOrderResult {}
}
