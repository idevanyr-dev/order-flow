package com.idevanyr.orderflow.order.api;

import com.idevanyr.orderflow.order.application.PlaceOrderUseCase;
import com.idevanyr.orderflow.order.application.PlacedOrderResult;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
class PlaceOrderController {

    private final PlaceOrderUseCase placeOrderUseCase;

    PlaceOrderController(PlaceOrderUseCase placeOrderUseCase) {
        this.placeOrderUseCase = placeOrderUseCase;
    }

    @PostMapping
    ResponseEntity<Object> place(@Valid @RequestBody PlaceOrderRequest request) {
        var result = placeOrderUseCase.execute(request.toCommand());

        return switch (result) {
            case PlacedOrderResult.Success(var orderId) ->
                    ResponseEntity.status(201).body(new PlaceOrderResponse(orderId));
            case PlacedOrderResult.ValidationError(var errors) ->
                    ResponseEntity.badRequest().body(new ErrorResponse(errors));
        };
    }
}
