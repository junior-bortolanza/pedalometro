package com.pedalometro.weather_api.client;

import com.pedalometro.weather_api.dto.GeoCodingResponseDTO;
import com.pedalometro.weather_api.exceptions.ExternalApiException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GeocodingClient {

    private static final Logger log = LoggerFactory.getLogger(GeocodingClient.class);
    private final RestClient restClient;

    public GeocodingClient(RestClient.Builder build) {
        this.restClient = build.build();
    }

    @CircuitBreaker(name = "externalApi", fallbackMethod = "fallbackGeocoding")
    @Retry(name = "externalApi")
    public GeoCodingResponseDTO searchFromCity(String city) {
        log.info("Iniciando chamada para Geocoding API para cidade: {}", city);
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("geocoding-api.open-meteo-inexistente.com")
                        .path("/v1/search")
                        .queryParam("name", city)
                        .queryParam("count", 1)
                        .queryParam("language", "pt")
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .body(GeoCodingResponseDTO.class);
    }

    public GeoCodingResponseDTO fallbackGeocoding(String city, Throwable t) {
        log.error("Erro na chamada para Geocoding API: {}", t.getMessage());
        throw new ExternalApiException("Serviço de geocodificação indisponível no momento.");
    }
}
