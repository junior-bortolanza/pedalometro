package com.pedalometro.weather_api.controller;

import com.pedalometro.weather_api.client.OpenMeteoClient;
import com.pedalometro.weather_api.dto.PedalometroResponseDTO;
import com.pedalometro.weather_api.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
@CrossOrigin("*")
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping
    public PedalometroResponseDTO getWeather(@RequestParam String city) {
        return weatherService.getWeather(city);
    }




}
