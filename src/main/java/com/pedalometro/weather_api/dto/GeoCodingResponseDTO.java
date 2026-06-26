package com.pedalometro.weather_api.dto;

import java.util.List;

public record GeoCodingResponseDTO(
        List<GeocodingResultsDTO> results
) {
}
