package com.pedalometro.weather_api.controller;

import com.pedalometro.weather_api.client.OpenMeteoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teste")
@RequiredArgsConstructor
public class TesteController {
    private final OpenMeteoClient openMeteoClient;




}
