package com.joffryferrater.opadatafilterspringbootstarter;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.joffryferrater.opadatafilterspringbootstarter.config.OpaConfig;
import com.joffryferrater.opadatafilterspringbootstarter.config.RestClientConfig;
import com.joffryferrater.opadatafilterspringbootstarter.core.deserializer.PredicateDeserializer;
import com.joffryferrater.opadatafilterspringbootstarter.model.response.Predicate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(
        value = {
                RestClientConfig.class
        }
)
@EnableConfigurationProperties(OpaConfig.class)
public class AppConfig {

    @Bean
    public SimpleModule opaCompilerResponseDeserializerModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Predicate.class, new PredicateDeserializer());
        return module;
    }
}
