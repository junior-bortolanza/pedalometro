package com.pedalometro.weather_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient.Builder restClient(PedalometroProperties properties) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(properties.getClient().getTimeout().getConnect());
        factory.setReadTimeout(properties.getClient().getTimeout().getRead());
        return RestClient.builder().requestFactory(factory);
    }
}
