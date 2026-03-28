package com.idevanyr.orderflow.order.domain;

import com.idevanyr.orderflow.order.application.PlaceOrderCommand;
import com.idevanyr.orderflow.order.application.PlaceOrderItemCommand;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderPolicy {

    public List<String> validateForPlacement(PlaceOrderCommand command) {
        var errors = new ArrayList<String>();

        if (command == null) {
            errors.add("command must be informed");
            return List.copyOf(errors);
        }

        if (command.customerId() == null || command.customerId().isBlank()) {
            errors.add("customerId must be informed");
        }

        if (command.items() == null || command.items().isEmpty()) {
            errors.add("order must contain at least one item");
            return List.copyOf(errors);
        }

        for (PlaceOrderItemCommand item : command.items()) {
            validateItem(errors, item);
        }

        return List.copyOf(errors);
    }

    private void validateItem(List<String> errors, PlaceOrderItemCommand item) {
        if (item == null) {
            errors.add("item must be informed");
            return;
        }

        if (item.productCode() == null || item.productCode().isBlank()) {
            errors.add("item productCode must be informed");
        }

        if (item.quantity() <= 0) {
            errors.add("item quantity must be greater than zero");
        }

        if (item.unitPrice() == null) {
            errors.add("item unitPrice must be informed");
            return;
        }

        if (item.unitPrice().signum() < 0) {
            errors.add("item unitPrice must be non-negative");
        }
    }
}
