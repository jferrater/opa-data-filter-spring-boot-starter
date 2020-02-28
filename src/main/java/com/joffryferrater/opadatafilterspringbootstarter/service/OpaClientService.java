package com.joffryferrater.opadatafilterspringbootstarter.service;

import com.joffryferrater.opadatafilterspringbootstarter.config.OpaConfig;
import com.joffryferrater.opadatafilterspringbootstarter.core.AstToSql;
import com.joffryferrater.opadatafilterspringbootstarter.model.request.PartialRequest;
import com.joffryferrater.opadatafilterspringbootstarter.model.response.OpaCompilerResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OpaClientService {

    private OpaConfig opaConfig;
    private RestTemplate opaClient;

    public OpaClientService(OpaConfig opaConfig, RestTemplate opaClient) {
        this.opaConfig = opaConfig;
        this.opaClient = opaClient;
    }

    public String getExecutableSqlStatements(PartialRequest partialRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PartialRequest> httpEntity = new HttpEntity<>(partialRequest, httpHeaders);
        ResponseEntity<OpaCompilerResponse> responseResponseEntity = opaClient.postForEntity(opaConfig.getUrl(), httpEntity, OpaCompilerResponse.class);
        AstToSql astToSql = new AstToSql(responseResponseEntity.getBody());
        return astToSql.getSqlQueryStatements(partialRequest);
    }
}
