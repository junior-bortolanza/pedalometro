package com.pedalometro.weather_api.controller;

import com.pedalometro.weather_api.dto.PedalometroResponseDTO;
import com.pedalometro.weather_api.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    public PedalometroResponseDTO getWeather(@RequestParam String city) {
        return weatherService.getWeather(city);
    }
}
