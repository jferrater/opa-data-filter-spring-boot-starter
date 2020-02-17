package com.joffryferrater.opadatafilterspringbootstarter.core;

import com.joffryferrater.opadatafilterspringbootstarter.model.response.Query;
import com.joffryferrater.opadatafilterspringbootstarter.model.response.Term;
import com.joffryferrater.opadatafilterspringbootstarter.model.response.Value;

import java.util.List;

public class AstUtil {

    public String toSqlQuery(Query query) {
        List<Term> terms = query.getTerms();
        Term operator = terms.get(0);
        Term leftExpression = terms.get(1);
        Term rightExpression = terms.get(2);
        String left = getExpression(leftExpression);
        String right = getExpression(rightExpression);
        return left + getOperator(operator) + right;
    }

    private String getOperator(Term term) {
        String value = (String) term.getValue();
        String operator = null;
        if("eq".equals(value)) {
            operator = "=";
        }
        return operator;
    }

    private String getExpression(Term term) {
        List<Value> values = (List<Value>)term.getValue();
        String expression = "";
        if ("ref".equals(term.getType())) {
            Value firstValue = values.get(1);
            Value secondValue = values.get(3);
            //expression = firstValue.getValue() + "." + secondValue.getValue();
        } else {
            String type = term.getType();
            Object objectValue = term.getValue();
            if ("string".equals(type)) {
                expression = (String) objectValue;
            } else if("number".equals(type)) {
                expression = String.valueOf(objectValue);
            }
        }
        return expression;
    }
}

