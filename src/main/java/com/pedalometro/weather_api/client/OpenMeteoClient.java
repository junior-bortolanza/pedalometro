package com.pedalometro.weather_api.client;

import com.pedalometro.weather_api.dto.OpenMeteoResponseDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OpenMeteoClient {
    private final RestClient restClient;

    public OpenMeteoClient(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public OpenMeteoResponseDTO getWeather(
            Double latitude,
            Double longitude) {

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
}
