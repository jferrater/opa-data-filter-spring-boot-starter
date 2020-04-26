package opa.datafilter.core.ast.db.query.config;

import opa.datafilter.core.ast.db.query.service.DefaultPartialRequest;
import opa.datafilter.core.ast.db.query.service.OpaClientService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;

import java.time.Duration;

/**
 * @author joffryferrater
 */
@Configuration
@Import(value = {
        OpaConfig.class,
        PartialRequestConfig.class
})
public class OpaDataCoreConfig {

    @Bean
    @RequestScope
    @Qualifier("defaultPartialRequest")
    public DefaultPartialRequest defaultPartialRequest() {
        return new DefaultPartialRequest();
    }

    @Bean
    @Qualifier("opaClientService")
    public <T> OpaClientService<T> opaClientService(OpaConfig opaConfig) {
        return new OpaClientService<>(opaConfig);
    }

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
