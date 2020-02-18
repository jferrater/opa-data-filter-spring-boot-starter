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
    private static final String TYPE = "type";
    private static final String VALUE = "value";
    private static final String NUMBER = "number";
    private static final String TERMS = "terms";
    private static final String STRING = "string";
    private static final String VAR = "var";
    private static final String INDEX = "index";

    @Override
    public Query deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec objectCodec = jsonParser.getCodec();
        JsonNode jsonNode = objectCodec.readTree(jsonParser);
        JsonNode termsNode = jsonNode.get(TERMS);
        List<Term> terms = new ArrayList<>();
        for (JsonNode termNode : termsNode) {
            Term term = new Term();
            String type = termNode.get(TYPE).asText();
            term.setType(type);
            JsonNode valueNodes = termNode.get(VALUE);
            if (valueNodes.isArray()) {
                List<Value> valueList = new ArrayList<>();
                for (JsonNode valueNode : valueNodes) {
                    Value value = getValue(valueNode);
                    valueList.add(value);
                }
                term.setValue(valueList);
            } else {
                setValueForTerm(valueNodes, term);
            }
            terms.add(term);
        }
        return buildQuery(jsonNode, terms);
    }

    private Value getValue(JsonNode valueNode) {
        Value value = new Value();
        String valueType = valueNode.get(TYPE).asText();
        value.setType(valueType);
        JsonNode nodeValue = valueNode.get(VALUE);
        if (NUMBER.equals(valueType)) {
            value.setValues(nodeValue.asInt());
        } else if (STRING.equals(valueType)) {
            value.setValues(nodeValue.asText());
        } else if (VAR.equals(valueType)) {
            value.setValues(nodeValue.asText());
        } else {
            LOGGER.warn("Value type '{}' is not yet supported", valueNode.toPrettyString());
        }
        return value;
    }

    private void setValueForTerm(JsonNode jsonNode, Term term) {
        if (jsonNode.isInt()) {
            term.setValue(jsonNode.asInt());
        } else if (jsonNode.isTextual()) {
            term.setValue(jsonNode.asText());
        } else {
            LOGGER.warn("Query type '{}' is not yet supported", jsonNode.toPrettyString());
        }
    }

    private Query buildQuery(JsonNode jsonNode, List<Term> terms) {
        Query query = new Query();
        JsonNode indexNode = jsonNode.get(INDEX);
        query.setIndex(indexNode.asInt());
        query.setTerms(terms);
        return query;
    }
}
