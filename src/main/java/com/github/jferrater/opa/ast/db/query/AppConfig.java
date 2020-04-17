package com.github.jferrater.opa.ast.db.query;

import com.github.jferrater.opa.ast.db.query.config.OpaConfig;
import com.github.jferrater.opa.ast.db.query.service.DefaultPartialRequest;
import com.github.jferrater.opa.ast.db.query.service.OpaClientService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
@Import(value = OpaConfig.class)
public class AppConfig {

    @Bean
    @RequestScope
    @Qualifier("defaultPartialRequest")
    public DefaultPartialRequest defaultPartialRequest() {
        return new DefaultPartialRequest();
    }

    @Bean
    public OpaClientService opaClientService(OpaConfig opaConfig) {
        return new OpaClientService(opaConfig);
    }
}
