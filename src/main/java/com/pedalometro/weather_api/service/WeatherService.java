package com.pedalometro.weather_api.service;

import com.pedalometro.weather_api.client.OpenMeteoClient;
import com.pedalometro.weather_api.dto.PedalometroResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PedaloMetroService {

    private final OpenMeteoClient openMeteoClient;

    public PedalometroResponseDTO searchWeather(String city) {

    }
}
