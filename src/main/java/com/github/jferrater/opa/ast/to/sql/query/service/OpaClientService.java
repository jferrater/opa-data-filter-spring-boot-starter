package com.github.jferrater.opa.ast.to.sql.query.service;

import com.github.jferrater.opa.ast.to.sql.query.config.OpaConfig;
import com.github.jferrater.opa.ast.to.sql.query.core.AstToSql;
import com.github.jferrater.opa.ast.to.sql.query.exception.OpaClientException;
import com.github.jferrater.opa.ast.to.sql.query.model.request.PartialRequest;
import com.github.jferrater.opa.ast.to.sql.query.model.response.OpaCompilerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpaClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpaClientService.class);
    private static final int TIMEOUT = 5_000;

    private OpaConfig opaConfig;
    private RestTemplate opaClient;

    public OpaClientService(OpaConfig opaConfig) {
        this.opaConfig = opaConfig;
        this.opaClient = opaClient();
    }

    public OpaClientService(OpaConfig opaConfig, RestTemplate restTemplate) {
        this.opaConfig = opaConfig;
        this.opaClient = restTemplate;
    }

    public String getExecutableSqlStatements(PartialRequest partialRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PartialRequest> httpEntity = new HttpEntity<>(partialRequest, httpHeaders);
        LOGGER.info("Sending partial request to Open Policy Agent server");
        ResponseEntity<OpaCompilerResponse> responseResponseEntity = opaClient.postForEntity(opaConfig.getUrl(), httpEntity, OpaCompilerResponse.class);
        if(200 != responseResponseEntity.getStatusCodeValue()) {
            int statusCode = responseResponseEntity.getStatusCodeValue();
            String message = String.format("Open Policy Agent server returns an error: %d", statusCode);
            throw new OpaClientException(message);
        }
        AstToSql astToSql = new AstToSql(responseResponseEntity.getBody());
        String sqlQueryStatements = astToSql.getSqlQueryStatements(partialRequest);
        LOGGER.info("Sql query from OPA partial request: {}", sqlQueryStatements);
        return sqlQueryStatements;
    }

    private static RestTemplate opaClient() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(TIMEOUT);
        clientHttpRequestFactory.setReadTimeout(TIMEOUT);
        return new RestTemplate(clientHttpRequestFactory);
    }
}
