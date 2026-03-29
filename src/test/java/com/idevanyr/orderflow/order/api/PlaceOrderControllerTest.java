package com.idevanyr.orderflow.order.api;

import com.idevanyr.orderflow.order.application.PlaceOrderUseCase;
import com.idevanyr.orderflow.order.application.PlacedOrderResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlaceOrderController.class)
class PlaceOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PlaceOrderUseCase placeOrderUseCase;

    @Test
    void shouldReturnCreatedWhenUseCaseSucceeds() throws Exception {
        when(placeOrderUseCase.execute(any()))
                .thenReturn(new PlacedOrderResult.Success(1L));

        var request = new PlaceOrderRequest(
                "C-100",
                List.of(new PlaceOrderItemRequest("P-10", 2, new java.math.BigDecimal("49.90")))
        );

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value(1));
    }

    @Test
    void shouldReturnBadRequestWhenUseCaseReturnsValidationError() throws Exception {
        when(placeOrderUseCase.execute(any()))
                .thenReturn(new PlacedOrderResult.ValidationError(List.of("order must contain at least one item")));

        var request = """
                {
                  "customerId": "C-100",
                  "items": []
                }
                """;

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("order must contain at least one item"));
    }
}
