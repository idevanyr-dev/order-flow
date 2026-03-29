package com.idevanyr.orderflow;

import com.idevanyr.orderflow.support.PostgresTestcontainersConfiguration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(PostgresTestcontainersConfiguration.class)
class ActuatorEndpointsIntegrationTest {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @LocalServerPort
    private int port;

    @Test
    void shouldExposeBasicHealthAndMetricsEndpoints() throws Exception {
        Map<String, Object> health = getJson("/actuator/health");
        Map<String, Object> liveness = getJson("/actuator/health/liveness");
        Map<String, Object> readiness = getJson("/actuator/health/readiness");
        Map<String, Object> metrics = getJson("/actuator/metrics");

        assertThat(health).containsEntry("status", "UP");
        assertThat(liveness).containsEntry("status", "UP");
        assertThat(readiness).containsEntry("status", "UP");
        assertThat(metrics.get("names")).isInstanceOf(List.class);
        assertThat((List<?>) metrics.get("names")).isNotEmpty();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getJson(String path) throws Exception {
        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + path))
                .GET()
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(200);
        return objectMapper.readValue(response.body(), Map.class);
    }
}
