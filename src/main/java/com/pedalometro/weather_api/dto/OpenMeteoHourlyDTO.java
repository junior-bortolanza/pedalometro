package com.pedalometro.weather_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OpenMeteoHourlyDTO(
        List<String> time,
        @JsonProperty("precipitation_probability")
        List<Integer> precipitationProbability,
        @JsonProperty("wind_speed_10m")
        List<Double> windSpeed) {
}
