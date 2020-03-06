package com.joffer.opa.ast.to.sql.query;

import com.joffer.opa.ast.to.sql.query.config.OpaConfig;
import com.joffer.opa.ast.to.sql.query.service.OpaClientService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = OpaConfig.class)
public class AppConfig {

    @Bean
    public OpaClientService opaClientService(OpaConfig opaConfig) {
        return new OpaClientService(opaConfig);
    }
}
