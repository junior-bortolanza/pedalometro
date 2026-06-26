package com.pedalometro.weather_api.client;

import com.pedalometro.weather_api.dto.GeoCodingResponseDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GeocodingClient {

    private final RestClient restClient;

    public GeocodingClient(RestClient.Builder build) {
        this.restClient = build.build();
    }

    public GeoCodingResponseDTO searchFromCity(String city) {

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("geocoding-api.open-meteo.com")
                        .path("/v1/search")
                        .queryParam("name", city)
                        .queryParam("count", 1)
                        .queryParam("language", "pt")
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .body(GeoCodingResponseDTO.class);
    }
}
