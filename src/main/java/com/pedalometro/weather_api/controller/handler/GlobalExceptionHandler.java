package com.pedalometro.weather_api.controller.handler;

import com.pedalometro.weather_api.exceptions.*;
import com.pedalometro.weather_api.exceptions.dto.ErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleCityNotFoundException(CityNotFoundException ex,
                                                                    HttpServletRequest request){
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildErrorMessage(HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getRequestURI(),
                "Cidade nao encontrada"
        ));
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ErrorDTO> handleExternalApiException(ExternalApiException ex, HttpServletRequest request){
        return  ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(buildErrorMessage(HttpStatus.SERVICE_UNAVAILABLE.value(),
                ex.getMessage(),
                request.getRequestURI(),
                "Serviço de geocodificação indisponível no momento."
        ));
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<ErrorDTO> handleInvalidDataException(InvalidDateException ex, HttpServletRequest request){
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrorMessage(HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getRequestURI(),
                "Data Invalida"
        ));
    }

    @ExceptionHandler(InvalidWeatherDataException.class)
    public ResponseEntity<ErrorDTO> handleInvalidWeatherDataException(InvalidWeatherDataException ex, HttpServletRequest request){
        return  ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(buildErrorMessage(HttpStatus.BAD_GATEWAY.value(),
                ex.getMessage(),
                request.getRequestURI(),
                "Dados climáticos inválidos"
        ));
    }

    @ExceptionHandler(WeatherDataNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleWeatherDataNotFoundException(WeatherDataNotFoundException ex, HttpServletRequest request){
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildErrorMessage(HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getRequestURI(),
                "Dados climáticos não encontrados"
        ));
    }

    private ErrorDTO buildErrorMessage(int status, String message, String path, String error) {
        return ErrorDTO.builder()
                .timestamp(LocalDateTime.now())
                .messageError(message)
                .error(error)
                .status(status)
                .path(path)
                .build();
    }

}
