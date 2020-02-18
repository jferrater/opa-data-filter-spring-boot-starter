package com.joffryferrater.opadatafilterspringbootstarter.core;

import com.joffryferrater.opadatafilterspringbootstarter.model.response.Query;
import com.joffryferrater.opadatafilterspringbootstarter.model.response.Term;
import com.joffryferrater.opadatafilterspringbootstarter.model.response.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class AstToSqlQueryConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AstToSqlQueryConverter.class);

    public String astQueryToSqlQuery(Query query) {
        List<Term> terms = query.getTerms();
        Term operator = terms.get(0);
        Term leftExpression = terms.get(1);
        Term rightExpression = terms.get(2);
        String left = getExpression(leftExpression);
        String right = getExpression(rightExpression);
        return left + getOperator(operator) + right;
    }

    private String getExpression(Term term) {
        Object termValue = term.getValue();
        String expression = "";
        if (termValue instanceof ArrayList) {
            List<Value> valueList = (List<Value>) termValue;
            Value firstValue = valueList.get(1);
            Value secondValue = valueList.get(3);
            expression = firstValue.getValues() + "." + secondValue.getValues();
        } else if (termValue instanceof String) {
            expression = (String) termValue;
        } else if (termValue instanceof Integer) {
            expression = String.valueOf(termValue);
        } else {
            LOGGER.warn("Unknown value type '{}'", term.getType());
        }
        return expression;
    }

    private String getOperator(Term term) {
        Object termValue = term.getValue();
        List<Value> valueList;
        Value valueObject = null;
        if (termValue instanceof ArrayList) {
            valueList = (List<Value>) termValue;
            valueObject = valueList.get(0);
        } else {
            valueObject = (Value) termValue;
        }
        String value = (String) valueObject.getValues();
        return getOperatorFromValue(value);
    }

    private String getOperatorFromValue(String value) {
        String operator = null;
        switch (value) {
            case "eq":
                operator = "=";
                break;
            case "gt":
                operator = ">";
                break;
            case "lt":
                operator = "<";
                break;
            default:
                LOGGER.warn("Operator '{}' is not yet supported", value);
        }
        return operator;
    }
}

