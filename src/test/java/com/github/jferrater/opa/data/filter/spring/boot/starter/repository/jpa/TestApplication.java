package com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(basePackages = {"com.github.jferrater.opa.data.filter.spring.boot.starter",
"com.github.jferrater.opa.ast.db.query.config"})
@Import(MyJpaConfig.class)
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

}
