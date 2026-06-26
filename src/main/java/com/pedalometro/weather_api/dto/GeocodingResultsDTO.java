package com.pedalometro.weather_api.dto;


public record GeocodingResultsDTO(
        String name,
        Double latitude,
        Double longitude,
        String date,
        String country
) {
}
