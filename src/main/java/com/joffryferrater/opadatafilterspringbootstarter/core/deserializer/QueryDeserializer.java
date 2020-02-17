package com.joffryferrater.opadatafilterspringbootstarter.core.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.joffryferrater.opadatafilterspringbootstarter.model.response.Query;
import com.joffryferrater.opadatafilterspringbootstarter.model.response.Term;
import com.joffryferrater.opadatafilterspringbootstarter.model.response.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QueryDeserializer extends JsonDeserializer<Query> {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryDeserializer.class);

    @Override
    public Query deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec objectCodec = jsonParser.getCodec();
        JsonNode jsonNode = objectCodec.readTree(jsonParser);
        JsonNode termsNode = jsonNode.get("terms");
        List<Term> terms = new ArrayList<>();
        for (JsonNode termNode : termsNode) {
            Term term = new Term();
            String type = termNode.get("type").asText();
            term.setType(type);
            JsonNode valueNodes = termNode.get("value");
            if (valueNodes.isArray()) {
                List<Value> valueList = new ArrayList<>();
                for (JsonNode valueNode : valueNodes) {
                    Value value = new Value();
                    String valueType = valueNode.get("type").asText();
                    value.setType(valueType);
                    if ("number".equals(valueType)) {
                        value.setValues(valueNode.get("value").asInt());
                    } else if ("string".equals(valueType)) {
                        value.setValues(valueNode.get("value").asText());
                    }
                    valueList.add(value);
                }
                term.setValue(valueList);
            } else if (valueNodes.isInt()) {
                term.setValue(valueNodes.asInt());
            } else if (valueNodes.isTextual()) {
                term.setValue(valueNodes.asText());
            } else {
                LOGGER.warn("Query type '{}' is not yet supported", valueNodes.toPrettyString());
            }
            terms.add(term);
        }
        Query query = new Query();
        JsonNode indexNode = jsonNode.get("index");
        query.setIndex(indexNode.asInt());
        query.setTerms(terms);
        return query;
    }
}
