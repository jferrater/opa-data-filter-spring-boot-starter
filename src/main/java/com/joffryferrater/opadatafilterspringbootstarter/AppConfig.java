package com.joffryferrater.opadatafilterspringbootstarter;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.joffryferrater.opadatafilterspringbootstarter.core.deserializer.PredicateDeserializer;
import com.joffryferrater.opadatafilterspringbootstarter.model.response.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public SimpleModule opaCompilerResponseDeserializerModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Predicate.class, new PredicateDeserializer());
        return module;
    }
}
