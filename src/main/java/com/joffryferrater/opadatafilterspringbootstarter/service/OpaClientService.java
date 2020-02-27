package com.joffryferrater.opadatafilterspringbootstarter.service;

import com.joffryferrater.opadatafilterspringbootstarter.config.OpaConfig;
import com.joffryferrater.opadatafilterspringbootstarter.model.request.PartialRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpaClientService {

    private OpaConfig opaConfig;
    private RestTemplate opaClient;

    public OpaClientService(OpaConfig opaConfig, RestTemplate opaClient) {
        this.opaConfig = opaConfig;
        this.opaClient = opaClient;
    }

    public String getExecutableSqlStatements(PartialRequest partialRequest) {
        return null;
    }
}
