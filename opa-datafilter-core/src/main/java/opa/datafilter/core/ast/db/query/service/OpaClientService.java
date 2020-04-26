package opa.datafilter.core.ast.db.query.service;

import opa.datafilter.core.ast.db.query.config.OpaConfig;
import opa.datafilter.core.ast.db.query.exception.OpaClientException;
import opa.datafilter.core.ast.db.query.model.request.PartialRequest;
import opa.datafilter.core.ast.db.query.model.response.OpaCompilerResponse;
import opa.datafilter.core.ast.db.query.sql.AstToSql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author joffryferrater
 *
 */
@Service
public class OpaClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpaClientService.class);

    private OpaConfig opaConfig;

    @Resource(name = "defaultPartialRequest")
    private DefaultPartialRequest defaultPartialRequest;

    @Autowired
    @Qualifier("opaClient")
    private RestTemplate opaClient;

    public OpaClientService(OpaConfig opaConfig) {
        this.opaConfig = opaConfig;
    }

    public OpaClientService(OpaConfig opaConfig, RestTemplate restTemplate) {
        this.opaConfig = opaConfig;
        this.opaClient = restTemplate;
    }

    /**
     * Sends the default {@link PartialRequest} to the Open Policy Agent server and receives the response.
     * The response is translated into SQL query statements in string format
     *
     * @return {@link String} Returns the SQL query statements
     */
    public String getExecutableSqlStatements() {
        return getExecutableSqlStatements(defaultPartialRequest.getDefaultPartialRequest());
    }

    /**
     * Sends the {@link PartialRequest} to the Open Policy Agent server and receives the response.
     * The response is translated into SQL query statements in string format
     *
     * @param partialRequest {@link PartialRequest}
     * @return {@link String} Returns the SQL query statements
     */
    public String getExecutableSqlStatements(PartialRequest partialRequest) {
        ResponseEntity<OpaCompilerResponse> responseResponseEntity = getOpaCompilerResponse(partialRequest);
        checkResponse(responseResponseEntity);
        AstToSql astToSql = new AstToSql(responseResponseEntity.getBody());
        String sqlQueryStatements = astToSql.getSqlQueryStatements(partialRequest);
        LOGGER.info("Sql query from OPA partial request: {}", sqlQueryStatements);
        return sqlQueryStatements;
    }

    /**
     * Get the response from Open Policy Agent compile api
     *
     * @return {@link OpaCompilerResponse}
     */
    public OpaCompilerResponse getOpaCompilerApiResponse() {
        PartialRequest partialRequest = defaultPartialRequest.getDefaultPartialRequest();
        return  getOpaCompilerApiResponse(partialRequest);
    }

    /**
     * Get the response from Open Policy Agent compile api
     * @param partialRequest {@link PartialRequest}
     * @return {@link OpaCompilerResponse}
     */
    public OpaCompilerResponse getOpaCompilerApiResponse(PartialRequest partialRequest) {
        ResponseEntity<OpaCompilerResponse> responseResponseEntity = getOpaCompilerResponse(partialRequest);
        checkResponse(responseResponseEntity);
        return  responseResponseEntity.getBody();
    }

    private ResponseEntity<OpaCompilerResponse> getOpaCompilerResponse(PartialRequest partialRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PartialRequest> httpEntity = new HttpEntity<>(partialRequest, httpHeaders);
        LOGGER.info("Sending partial request to Open Policy Agent server");
        return opaClient.postForEntity(opaConfig.getUrl(), httpEntity, OpaCompilerResponse.class);
    }

    private void checkResponse(ResponseEntity<OpaCompilerResponse> responseResponseEntity) {
        if(200 != responseResponseEntity.getStatusCodeValue()) {
            int statusCode = responseResponseEntity.getStatusCodeValue();
            String message = String.format("Open Policy Agent server returns an error: %d", statusCode);
            throw new OpaClientException(message);
        }
    }
}
