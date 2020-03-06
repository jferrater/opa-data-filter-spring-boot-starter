package com.github.jferrater.opa.ast.to.sql.query.core;

import com.github.jferrater.opa.ast.to.sql.query.model.response.Value;
import com.github.jferrater.opa.ast.to.sql.query.core.elements.SqlPredicate;
import com.github.jferrater.opa.ast.to.sql.query.model.response.Predicate;
import com.github.jferrater.opa.ast.to.sql.query.model.response.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts Open Policy Agent Abstract Syntax Tree predicate into SQL predicate.
 */
public class PredicateConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PredicateConverter.class);

    private Predicate predicate;

    public PredicateConverter(Predicate predicate) {
        this.predicate = predicate;
    }

    public SqlPredicate astToSqlPredicate() {
        SqlPredicate sqlPredicate = new SqlPredicate();
        List<Term> terms = predicate.getTerms();
        Term operator = terms.get(0);
        String operatorString = getOperator(operator);
        sqlPredicate.setOperator(operatorString);
        Object termValue1 = terms.get(1).getValue();
        Object termValue2 = terms.get(2).getValue();
        if(termValue1 instanceof ArrayList) {
            String left = getExpression(termValue1);
            sqlPredicate.setLeftExpression(left);
            String right = getExpression(termValue2);
            sqlPredicate.setRightExpression(right);
        } else {
            String left = getExpression(termValue2);
            sqlPredicate.setLeftExpression(left);
            String right = getExpression(termValue1);
            sqlPredicate.setRightExpression(right);
        }
        LOGGER.info("SQL predicate: {}", sqlPredicate.toString());
        return sqlPredicate;
    }

    private String getExpression(Object termValue) {
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
            LOGGER.warn("Unknown value type");
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

