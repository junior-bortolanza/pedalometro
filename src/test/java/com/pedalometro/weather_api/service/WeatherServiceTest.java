package com.pedalometro.weather_api.service;

import com.pedalometro.weather_api.client.GeocodingClient;
import com.pedalometro.weather_api.client.OpenMeteoClient;
import com.pedalometro.weather_api.dto.*;
import com.pedalometro.weather_api.exceptions.CityNotFoundException;
import com.pedalometro.weather_api.exceptions.ExternalApiException;
import com.pedalometro.weather_api.exceptions.InvalidWeatherDataException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private OpenMeteoClient openMeteoClient;
    @Mock
    private GeocodingClient geocodingClient;
    @Mock
    private WeatherScoringService scoringService;
    @Mock
    private WeatherMessageService messageService;

    @InjectMocks
    private WeatherService weatherService;

    @Test
    void getWeather_Success() {
        String city = "Sorocaba";
        GeoCodingResponseDTO geoResponse = new GeoCodingResponseDTO(List.of(
                new GeocodingResultsDTO("Sorocaba", -23.5, -47.45, "2026-06-26", "Brazil")
        ));
        when(geocodingClient.searchFromCity(city)).thenReturn(geoResponse);

        OpenMeteoResponseDTO weatherResponse = new OpenMeteoResponseDTO(
                new OpenMeteoHourlyDTO(List.of("2026-06-26T10:00"), List.of(50), List.of(30.0)),
                new OpenMeteoDailyDTO(List.of("06:00"), List.of("18:00"))
        );
        when(openMeteoClient.getWeather(-23.5, -47.45)).thenReturn(weatherResponse);
        
        when(scoringService.calculateScore(anyInt(), anyDouble())).thenReturn(60);
        when(scoringService.getStatus(60)).thenReturn("BOM");
        when(messageService.getMessage(60)).thenReturn("Boa mensagem");

        PedalometroResponseDTO response = weatherService.getWeather(city);

        assertNotNull(response);
        assertEquals(city, response.city());
        assertEquals("BOM", response.status());
        verify(geocodingClient).searchFromCity(city);
        verify(openMeteoClient).getWeather(-23.5, -47.45);
    }

    @Test
    void getWeather_GeocodingError_ThrowsExternalApiException() {
        String city = "Sorocaba";
        when(geocodingClient.searchFromCity(city)).thenThrow(new RuntimeException("API Error"));

        assertThrows(ExternalApiException.class, () -> weatherService.getWeather(city));
    }

    @Test
    void getWeather_CityNotFound_ThrowsCityNotFoundException() {
        String city = "Unknown";
        when(geocodingClient.searchFromCity(city)).thenReturn(new GeoCodingResponseDTO(List.of()));

        assertThrows(CityNotFoundException.class, () -> weatherService.getWeather(city));
    }

    @Test
    void getWeather_WeatherError_ThrowsExternalApiException() {
        String city = "Sorocaba";
        GeoCodingResponseDTO geoResponse = new GeoCodingResponseDTO(List.of(
                new GeocodingResultsDTO("Sorocaba", -23.5, -47.45, "2026-06-26", "Brazil")
        ));
        when(geocodingClient.searchFromCity(city)).thenReturn(geoResponse);
        when(openMeteoClient.getWeather(-23.5, -47.45)).thenThrow(new RuntimeException("API Error"));

        assertThrows(ExternalApiException.class, () -> weatherService.getWeather(city));
    }

    @Test
    void getWeather_InvalidWeatherData_ThrowsInvalidWeatherDataException() {
        String city = "Sorocaba";
        GeoCodingResponseDTO geoResponse = new GeoCodingResponseDTO(List.of(
                new GeocodingResultsDTO("Sorocaba", -23.5, -47.45, "2026-06-26", "Brazil")
        ));
        when(geocodingClient.searchFromCity(city)).thenReturn(geoResponse);
        
        // Incomplete weather data
        OpenMeteoResponseDTO weatherResponse = new OpenMeteoResponseDTO(null, null);
        when(openMeteoClient.getWeather(-23.5, -47.45)).thenReturn(weatherResponse);

        assertThrows(InvalidWeatherDataException.class, () -> weatherService.getWeather(city));
    }
}
