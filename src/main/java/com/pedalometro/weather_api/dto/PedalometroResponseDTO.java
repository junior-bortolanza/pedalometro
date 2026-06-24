package com.pedalometro.weather_api.dto;

import java.util.List;

public record PedalometroResponseDTO(
        String city,
        String status,
        Integer score,
        String message,
        Integer rainChance,
        Double windSpeed,
        String sunrise,
        String sunset,
        String badTime,
        List<HourlyForecastsDTO> hourlyForecasts
) {
}
