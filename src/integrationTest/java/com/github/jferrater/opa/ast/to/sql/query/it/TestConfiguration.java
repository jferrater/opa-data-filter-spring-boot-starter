package com.github.jferrater.opa.ast.to.sql.query.it;

import com.github.jferrater.opa.ast.to.sql.query.config.OpaConfig;
import com.github.jferrater.opa.ast.to.sql.query.service.OpaClientService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TestConfiguration {

    @Bean
    public OpaClientService opaClientService() {
        OpaConfig opaConfig = new OpaConfig();
        opaConfig.setUrl("http://localhost:8181/v1/compile");
        opaConfig.setDataFilterEnabled(true);
        return new OpaClientService(opaConfig);
    }

    @Bean
    @Qualifier("opaClient")
    public RestTemplate opaClient() {
        return new RestTemplate();
    }
}
