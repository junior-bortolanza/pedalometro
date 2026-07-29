package com.pedalometro.weather_api.service;

import com.pedalometro.weather_api.config.PedalometroProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherScoringService {

    private final PedalometroProperties properties;

    public Integer calculateScore(Integer rainChance, Double windSpeed) {
        int score = 100;
        var rain = properties.getScore().getRain();
        var wind = properties.getScore().getWind();

        if (rainChance >= rain.getHigh()) {
            score -= rain.getHighPenalty();
        } else if (rainChance >= rain.getMedium()) {
            score -= rain.getMediumPenalty();
        } else if (rainChance >= rain.getLow()) {
            score -= rain.getLowPenalty();
        }

        if (windSpeed >= wind.getHigh()) {
            score -= wind.getHighPenalty();
        } else if (windSpeed >= wind.getMedium()) {
            score -= wind.getMediumPenalty();
        } else if (windSpeed >= wind.getLow()) {
            score -= wind.getLowPenalty();
        }

        return Math.max(score, 0);
    }

    public String getStatus(Integer score) {
        var status = properties.getScore().getStatus();
        if (score >= status.getExcellent()) {
            return "EXCELENTE";
        }
        if (score >= status.getGood()) {
            return "BOM";
        }
        if (score >= status.getRegular()) {
            return "REGULAR";
        }
        return "RUIM";
    }
}
