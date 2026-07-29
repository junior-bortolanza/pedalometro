package com.pedalometro.weather_api.client;

import com.pedalometro.weather_api.dto.OpenMeteoResponseDTO;
import com.pedalometro.weather_api.exceptions.ExternalApiException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OpenMeteoClient {
    private static final Logger log = LoggerFactory.getLogger(OpenMeteoClient.class);
    private final RestClient restClient;

    public OpenMeteoClient(RestClient.Builder builder) {
        this.restClient = builder.build();
    }
    @Retry(name = "externalApi")
    @CircuitBreaker(name = "externalApi", fallbackMethod = "fallbackWeather")
    public OpenMeteoResponseDTO getWeather(
            Double latitude,
            Double longitude) {
        log.info("Iniciando chamada para OpenMeteo API para lat: {}, lon: {}", latitude, longitude);
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.open-meteo.com")
                        .path("/v1/forecast")
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam("current",
                                "temperature_2m,wind_speed_10m")
                        .queryParam("hourly",
                                "precipitation_probability,wind_speed_10m")
                        .queryParam("daily", "sunrise,sunset")
                        .queryParam("forecast_days", 1)
                        .queryParam("timezone", "auto")
                        .build())
                .retrieve()
                .body(OpenMeteoResponseDTO.class);
    }

    public OpenMeteoResponseDTO fallbackWeather(Double latitude, Double longitude, Throwable t) {
        log.error("=============================");
        log.error("FALLBACK EXECUTADO");
        log.error("Motivo: {}", t.getMessage());
        log.error("=============================");

        throw new ExternalApiException("Serviço indisponível.");
    }
}
