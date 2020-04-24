package com.github.jferrater.opadatafilterjpaspringbootstarter.repository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @author joffryferrater
 */
@SpringBootApplication
@ComponentScan(basePackages = {
        "com.github.jferrater.opadatafilterjpaspringbootstarter",
        "com.github.jferrater.opadatafilterjpaspringbootstarter.config"
})
@Import(MyJpaConfig.class)
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

}
