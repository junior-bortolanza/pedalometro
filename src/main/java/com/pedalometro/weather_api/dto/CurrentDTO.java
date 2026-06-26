package com.pedalometro.weather_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrentDTO(
        @JsonProperty("temperature_2m")
        Double temperature,
        @JsonProperty("wind_speed_10m")
        Double winSpeed

) {
}
