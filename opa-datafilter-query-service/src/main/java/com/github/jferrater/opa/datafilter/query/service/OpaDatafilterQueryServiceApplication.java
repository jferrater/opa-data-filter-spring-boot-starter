package com.github.jferrater.opa.datafilter.query.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * @author joffryferrater
 */
@SpringBootApplication(exclude={MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class OpaDatafilterQueryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpaDatafilterQueryServiceApplication.class, args);
	}
}
