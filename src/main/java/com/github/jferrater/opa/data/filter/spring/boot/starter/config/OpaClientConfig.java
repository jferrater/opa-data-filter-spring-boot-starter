package com.github.jferrater.opa.data.filter.spring.boot.starter.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class OpaClientConfig {

    @Bean
    @Qualifier("opaClient")
    public RestTemplate opaClient(RestTemplateBuilder opaRestTemplateBuilder) {
        return opaRestTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(5_000))
                .setReadTimeout(Duration.ofSeconds(5_000))
                .build();
    }

    @Bean
    @Qualifier("opaRestTemplateBuilder")
    public RestTemplateBuilder opaRestTemplateBuilder() {
        return new RestTemplateBuilder();
    }
}
