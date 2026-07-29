package com.pedalometro.weather_api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "pedalometro")
@Getter
@Setter
public class PedalometroProperties {

    private ScoreConfig score;
    private ClientConfig client;

    @Getter @Setter
    public static class ScoreConfig {
        private RainConfig rain;
        private WindConfig wind;
        private StatusConfig status;
    }

    @Getter @Setter
    public static class ClientConfig {
        private TimeoutConfig timeout;
    }

    @Getter @Setter
    public static class TimeoutConfig {
        private int connect;
        private int read;
    }

    @Getter @Setter
    public static class RainConfig {
        private int high;
        private int medium;
        private int low;
        private int highPenalty;
        private int mediumPenalty;
        private int lowPenalty;
    }

    @Getter @Setter
    public static class WindConfig {
        private int high;
        private int medium;
        private int low;
        private int highPenalty;
        private int mediumPenalty;
        private int lowPenalty;
    }

    @Getter @Setter
    public static class StatusConfig {
        private int excellent;
        private int good;
        private int regular;
    }
}
