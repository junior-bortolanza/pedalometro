package com.pedalometro.weather_api.service;

import com.pedalometro.weather_api.client.GeocodingClient;
import com.pedalometro.weather_api.client.OpenMeteoClient;
import com.pedalometro.weather_api.dto.*;
import com.pedalometro.weather_api.exceptions.CityNotFoundException;
import com.pedalometro.weather_api.exceptions.WeatherDataNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class WeatherService {


    private final OpenMeteoClient openMeteoClient;
    private final GeocodingClient geocodingClient;

    public WeatherService(OpenMeteoClient openMeteoClient, GeocodingClient geocodingClient) {
        this.openMeteoClient = openMeteoClient;
        this.geocodingClient = geocodingClient;
    }

    public PedalometroResponseDTO getWeather(String city) {

        GeoCodingResponseDTO geocoding = geocodingClient.searchFromCity(city);
        if(geocoding.results() == null || geocoding.results().isEmpty()) {
            throw new CityNotFoundException("Cidade não encontrada: " + city);
        }
        GeocodingResultsDTO result = geocoding.results().getFirst();

        Double latitude = result.latitude();
        Double longitude = result.longitude();


        OpenMeteoResponseDTO weather = openMeteoClient.getWeather(latitude, longitude);
        if(weather.hourly() == null
        || weather.hourly().windSpeed() == null
        || weather.hourly().windSpeed().isEmpty()) {
            throw new WeatherDataNotFoundException("Dados de vento não encontrados.");
        }

        Double windSpeed = weather.hourly().windSpeed().getFirst();
        Integer rainChance = weather.hourly().precipitationProbability().getFirst();

        Integer score = calculateScore(rainChance, windSpeed);

        String sunrise = weather.daily().sunrise().getFirst();
        String sunset = weather.daily().sunset().getFirst();

        List<HourlyForecastsDTO> hourlyForecasts = buildHourlyForecasts(weather);

        String badTime= getBadTime(hourlyForecasts);

        return new PedalometroResponseDTO(
                city,
                getStatus(score),
                score,
                getMessage(score),
                rainChance,
                windSpeed,
                sunrise,
                sunset,
                badTime,
                hourlyForecasts
        );
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

            Integer score = calculateScore(rainChance, windSpeed);

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

    private Integer calculateScore(Integer rainChance, Double windSpeed) {
        int score = 100;

            if(rainChance >= 80 ) {
                score -= 50;
            } else if (rainChance >= 50) {
                score -=30;
            } else if (rainChance >= 20) {
                score -= 10;
            }

            //Wind
            if(windSpeed >= 30) {
                score -=40;
            } else if(windSpeed >= 20) {
                score -= 25;
            }else if (windSpeed >= 10) {
                score -= 10;
            }

            return Math.max(score, 0);

    }

    private String getMessage(Integer score) {

        if(score >= 80) {
            return bestMessages();
        }

        if(score >= 60) {
            return goodMessages();
        }
        if(score >= 40) {
            return regularMessages();
        }
        return badMessages();
    }

    private String getStatus(Integer score) {
        if (score >= 80){
            return "EXCELENTE";
        }
        if (score >= 60){
            return "BOM";
        }

        if (score >= 40){
            return "REGULAR";
        }
        return "RUIM";
    }

    private String bestMessages() {
        List<String> messages = List.of(
                "Bora, Fuzilos! Hoje até o Alemão pode sair sem medo de chuva.",
                "Hoje o Raide não tem desculpa pra querer voltar com 30 km.",
                "Fino, acorda! O pedal tá liberado.",
                "Mario, pode até ir de elétrica, mas hoje o clima tá ajudando.",
                "Craudinho, hoje a cobra verde tirou folga.",
                "Rulio, nenhuma onça confirmou presença no percurso.",
                "Gar, deixa as latinhas pra depois do pedal.",
                "Primo, hoje aguenta firme que o clima tá ajudando.",
                "Hoje ninguém inventa desculpa. Só bora pedalar!",
                "Até São Pedro resolveu colaborar com os Fuzilos hoje."
        );
        int random = ThreadLocalRandom.current().nextInt(messages.size());
        return messages.get(random);
    }

    private String goodMessages() {
        List<String> messages = List.of(
                "Bora, Fuzilos! Hoje até o Alemão pode sair sem medo de chuva.",
                "Hoje o Raide não tem desculpa pra querer voltar com 30 km.",
                "Fino, acorda! O pedal tá liberado.",
                "Mario, pode até ir de elétrica, mas hoje o clima tá ajudando.",
                "Craudinho, hoje a cobra verde tirou folga.",
                "Rulio, nenhuma onça confirmou presença no percurso.",
                "Gar, deixa as latinhas pra depois do pedal.",
                "Primo, hoje aguenta firme que o clima tá ajudando.",
                "Hoje ninguém inventa desculpa. Só bora pedalar!",
                "Até São Pedro resolveu colaborar com os Fuzilos hoje."
        );
        int random = ThreadLocalRandom.current().nextInt(messages.size());
        return messages.get(random);
    }



    private String regularMessages() {
        List<String> messages = List.of(
                "Alemão já começou a olhar a previsão de chuva de novo.",
                "Raide já perguntou se dá pra mudar o rolê antes de sair.",
                "Rulio ouviu um barulho no mato e já voltou pra casa.",
                "Craudinho viu uma mangueira e achou que era cobra verde.",
                "Gar já tá procurando onde comprar a primeira latinha.",
                "Fino falou que vai... depois do almoço.",
                "Primo vai precisar lembrar que ainda faltam muitos quilômetros.",
                "Mario perguntou se tem tomada no meio do caminho.",
                "Hoje o pedal é por sua conta e risco, Fuzilo.",
                "O clima tá meio suspeito... hoje até o grupo do WhatsApp vai ficar dividido."
        );

        int random = ThreadLocalRandom.current().nextInt(messages.size());
        return messages.get(random);
    }
    private String badMessages() {
        List<String> messages = List.of(
                "Manga proibiu! Hoje é só resenha e cerveja.",
                "Alemão já cancelou por causa de uma nuvem no horizonte.",
                "Raide nem saiu de casa e já queria trocar o percurso.",
                "Rulio viu um cachorro e achou que era onça.",
                "Craudinho encontrou uma mangueira e pediu pra voltar.",
                "Gar já abriu a primeira latinha antes mesmo do horário.",
                "Fino mandou mensagem: 'me acorda no próximo pedal'.",
                "Mario colocou a elétrica pra carregar e foi dormir.",
                "Primo agradeceu o clima por dar uma desculpa pra descansar.",
                "Hoje até o Strava falou: melhor deixar pra amanhã."
        );

        int random = ThreadLocalRandom.current().nextInt(messages.size());
        return messages.get(random);
    }
}
