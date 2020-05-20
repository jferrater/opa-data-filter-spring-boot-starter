package com.github.jferrater.opa.datafilter.query.service;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OpaDatafilterQueryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpaDatafilterQueryServiceApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
