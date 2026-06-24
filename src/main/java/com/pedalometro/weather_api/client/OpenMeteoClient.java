package com.pedalometro.weather_api.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OpenMeteoClient {
    private final RestClient restClient;

    public OpenMeteoClient(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public String getWeather(Double latitude, Double longitude) {

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
                        .queryParam("timezone", "auto")
                        .build())
                .retrieve()
                .body(String.class);
    }

}
