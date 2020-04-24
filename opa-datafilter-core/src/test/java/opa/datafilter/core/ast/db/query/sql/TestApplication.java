package opa.datafilter.core.ast.db.query.sql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @author joffryferrater
 */
@SpringBootApplication
@ComponentScan(basePackages = {"opa.datafilter.core.ast.db.query.sql",
"opa.datafilter.core.ast.db.query.config"})
@Import(MyJpaConfig.class)
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

}
