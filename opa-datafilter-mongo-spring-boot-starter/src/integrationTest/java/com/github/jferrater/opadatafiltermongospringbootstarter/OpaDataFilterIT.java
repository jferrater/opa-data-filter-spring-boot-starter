package com.github.jferrater.opadatafiltermongospringbootstarter;

import com.github.jferrater.opadatafiltermongospringbootstarter.config.MongoConfig;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author joffryferrater
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MongoConfig.class, TestApp.class})
@ActiveProfiles("integrationTest")
public class OpaDataFilterIT {

    @Test
    void testContext() {

    }

}
