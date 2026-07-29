package com.pedalometro.weather_api.exceptions.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO{
    private LocalDateTime timestamp;
    private Integer status;
    private String messageError;
    private String error;
    private String path;

}
