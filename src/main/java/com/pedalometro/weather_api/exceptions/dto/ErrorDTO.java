package com.pedalometro.weather_api.exceptions.dto;

public record ErrorDTO(
        String timestamp,
        String status,
        String messageError,
        String path
) {
}
