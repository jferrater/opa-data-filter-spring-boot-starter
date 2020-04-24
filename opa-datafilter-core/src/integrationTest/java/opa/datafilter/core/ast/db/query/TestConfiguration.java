package opa.datafilter.core.ast.db.query;
import opa.datafilter.core.ast.db.query.config.OpaConfig;
import opa.datafilter.core.ast.db.query.service.DefaultPartialRequest;
import opa.datafilter.core.ast.db.query.service.OpaClientService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;

/**
 * @author joffryferrater
 */
@Configuration
public class TestConfiguration {

    @Bean
    public OpaClientService opaClientService() {
        OpaConfig opaConfig = new OpaConfig();
        opaConfig.setUrl("http://localhost:8181/v1/compile");
        opaConfig.setDataFilterEnabled(true);
        return new OpaClientService(opaConfig);
    }

    @Bean
    @Qualifier("opaClient")
    public RestTemplate opaClient() {
        return new RestTemplate();
    }

    @Bean
    @RequestScope
    @Qualifier("defaultPartialRequest")
    public DefaultPartialRequest defaultPartialRequest() {
        return new DefaultPartialRequest();
    }
}
