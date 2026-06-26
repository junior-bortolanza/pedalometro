package com.pedalometro.weather_api.dto;

import java.util.List;

public record OpenMeteoDailyDTO(
        List<String> sunrise,
        List<String> sunset
) {
}
