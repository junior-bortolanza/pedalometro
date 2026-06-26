package com.pedalometro.weather_api.dto;


public record OpenMeteoResponseDTO(
        OpenMeteoHourlyDTO hourly,
        OpenMeteoDailyDTO daily
) {
}
