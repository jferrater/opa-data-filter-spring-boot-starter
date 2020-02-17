package com.joffryferrater.opadatafilterspringbootstarter.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.joffryferrater.opadatafilterspringbootstarter.core.deserializer.QueryDeserializer;
import com.joffryferrater.opadatafilterspringbootstarter.model.response.Query;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public SimpleModule opaCompilerResponseDeserializerModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Query.class, new QueryDeserializer());
        return module;
    }
}
