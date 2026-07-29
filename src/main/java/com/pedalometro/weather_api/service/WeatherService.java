package com.pedalometro.weather_api.service;

import com.pedalometro.weather_api.client.GeocodingClient;
import com.pedalometro.weather_api.client.OpenMeteoClient;
import com.pedalometro.weather_api.dto.*;
import com.pedalometro.weather_api.exceptions.CityNotFoundException;
import com.pedalometro.weather_api.exceptions.ExternalApiException;
import com.pedalometro.weather_api.exceptions.InvalidWeatherDataException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {


    private final OpenMeteoClient openMeteoClient;
    private final GeocodingClient geocodingClient;
    private final WeatherScoringService scoringService;
    private final WeatherMessageService messageService;

    public WeatherService(OpenMeteoClient openMeteoClient, GeocodingClient geocodingClient, 
                          WeatherScoringService scoringService, WeatherMessageService messageService) {
        this.openMeteoClient = openMeteoClient;
        this.geocodingClient = geocodingClient;
        this.scoringService = scoringService;
        this.messageService = messageService;
    }

    public PedalometroResponseDTO getWeather(String city) {

        GeoCodingResponseDTO geocoding;
        try {
            geocoding = geocodingClient.searchFromCity(city);
        } catch (ExternalApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalApiException("Erro ao consultar serviço de geocodificação.");
        }

        validateGeocodingResponse(geocoding, city);
        
        GeocodingResultsDTO result = geocoding.results().getFirst();
        Double latitude = result.latitude();
        Double longitude = result.longitude();

        OpenMeteoResponseDTO weather;
        try {
            weather = openMeteoClient.getWeather(latitude, longitude);
        } catch (Exception e) {
            throw new ExternalApiException("Erro ao consultar serviço de previsão do tempo: " + e.getMessage());
        }

        validateWeatherResponse(weather);

        Double windSpeed = weather.hourly().windSpeed().getFirst();
        Integer rainChance = weather.hourly().precipitationProbability().getFirst();

        Integer score = scoringService.calculateScore(rainChance, windSpeed);

        String sunrise = weather.daily().sunrise().getFirst();
        String sunset = weather.daily().sunset().getFirst();

        List<HourlyForecastsDTO> hourlyForecasts = buildHourlyForecasts(weather);

        String badTime= getBadTime(hourlyForecasts);

        return new PedalometroResponseDTO(
                city,
                scoringService.getStatus(score),
                score,
                messageService.getMessage(score),
                rainChance,
                windSpeed,
                sunrise,
                sunset,
                badTime,
                hourlyForecasts
        );
    }

    private void validateGeocodingResponse(GeoCodingResponseDTO geocoding, String city) {
        if (geocoding == null || geocoding.results() == null || geocoding.results().isEmpty()) {
            throw new CityNotFoundException("Cidade \"" + city + "\" não encontrada.");
        }
    }

    private void validateWeatherResponse(OpenMeteoResponseDTO weather) {
        if (weather == null || weather.hourly() == null || weather.daily() == null ||
                weather.hourly().time() == null || weather.hourly().time().isEmpty() ||
                weather.hourly().windSpeed() == null || weather.hourly().windSpeed().isEmpty() ||
                weather.hourly().precipitationProbability() == null || weather.hourly().precipitationProbability().isEmpty() ||
                weather.daily().sunrise() == null || weather.daily().sunrise().isEmpty() ||
                weather.daily().sunset() == null || weather.daily().sunset().isEmpty()) {
            throw new InvalidWeatherDataException("A API de clima retornou dados incompletos.");
        }
    }

    private String getBadTime(List<HourlyForecastsDTO> forecasts) {
        HourlyForecastsDTO worst = forecasts.getFirst();

        for(HourlyForecastsDTO forecast : forecasts){
            if(forecast.score() < worst.score()) {
                worst = forecast;
            }
        }
        return formatTime(worst.time());
    }

    private String formatTime(String dateTime){
        return dateTime.substring(11, 16);
    }


    private List<HourlyForecastsDTO> buildHourlyForecasts(OpenMeteoResponseDTO weather) {

        List<HourlyForecastsDTO> forecasts = new ArrayList<>();

        for (int i = 0; i < weather.hourly().time().size(); i++) {

            String time = weather.hourly().time().get(i);
            Integer rainChance = weather.hourly().precipitationProbability().get(i);
            Double windSpeed = weather.hourly().windSpeed().get(i);

            Integer score = scoringService.calculateScore(rainChance, windSpeed);

            HourlyForecastsDTO hourlyForecast = new HourlyForecastsDTO(
                    time,
                    rainChance,
                    windSpeed,
                    score
            );

            forecasts.add(hourlyForecast);
        }

        return forecasts;
    }
}
