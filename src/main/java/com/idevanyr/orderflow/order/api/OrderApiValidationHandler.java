package com.idevanyr.orderflow.order.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;

@RestControllerAdvice(basePackageClasses = PlaceOrderController.class)
class OrderApiValidationHandler {

    @ExceptionHandler(BadRequestApiException.class)
    ResponseEntity<ErrorResponse> handleBadRequestApiException(BadRequestApiException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.errors()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        var errors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .toList();

        return ResponseEntity.badRequest().body(new ErrorResponse(errors));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    ResponseEntity<ErrorResponse> handleHandlerMethodValidation(HandlerMethodValidationException exception) {
        List<String> errors = exception.getParameterValidationResults().stream()
                .flatMap(result -> result.getResolvableErrors().stream()
                        .map(error -> error.getDefaultMessage()))
                .toList();

        return ResponseEntity.badRequest().body(new ErrorResponse(errors));
    }

    @ExceptionHandler(NotFoundApiException.class)
    ResponseEntity<Void> handleNotFoundApiException(NotFoundApiException exception) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(RejectedApiException.class)
    ResponseEntity<ReasonResponse> handleRejectedApiException(RejectedApiException exception) {
        return ResponseEntity.status(422).body(new ReasonResponse(exception.getMessage()));
    }

    @ExceptionHandler(UpstreamFailureApiException.class)
    ResponseEntity<ReasonResponse> handleUpstreamFailureApiException(UpstreamFailureApiException exception) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ReasonResponse(exception.getMessage()));
    }
}
