package com.pedalometro.weather_api.exceptions;

public class InvalidWeatherDataException extends RuntimeException {
    public InvalidWeatherDataException(String message) {
        super(message);
    }
}
