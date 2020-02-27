package com.joffryferrater.opadatafilterspringbootstarter.config;

import com.joffryferrater.opadatafilterspringbootstarter.service.OpaClientService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConditionalOnClass(OpaClientService.class)
public class RestClientConfig {

    private static final int TIMEOUT = 5_000;

    @Bean
    public RestTemplate opaClient() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(TIMEOUT);
        clientHttpRequestFactory.setReadTimeout(TIMEOUT);
        return new RestTemplate(clientHttpRequestFactory);
    }
}
