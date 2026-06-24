package com.pedalometro.weather_api.dto;

public record HourlyForecastsDTO(
        String time,
        Integer rainChance,
        Double windSpeed,
        Integer score

) {
}
