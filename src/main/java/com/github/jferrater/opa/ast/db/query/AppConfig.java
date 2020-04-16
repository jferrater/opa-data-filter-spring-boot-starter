package com.github.jferrater.opa.ast.db.query;

import com.github.jferrater.opa.ast.db.query.config.OpaConfig;
import com.github.jferrater.opa.ast.db.query.service.OpaClientService;
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
