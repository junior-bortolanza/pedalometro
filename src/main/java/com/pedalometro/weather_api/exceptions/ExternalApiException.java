package com.pedalometro.weather_api.exceptions;

public class ExternalApiException extends RuntimeException {
  public ExternalApiException(String message) {
    super(message);
  }
}
