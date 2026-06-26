package com.pedalometro.weather_api.dto;

import java.util.List;

public record GeocodingResultsDTO(
        List<GeoCodingResponseDTO> results
) {
}
