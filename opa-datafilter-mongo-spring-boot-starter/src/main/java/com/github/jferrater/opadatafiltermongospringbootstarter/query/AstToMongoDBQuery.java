package com.github.jferrater.opadatafiltermongospringbootstarter.query;

import opa.datafilter.core.ast.db.query.PredicateConverter;
import opa.datafilter.core.ast.db.query.elements.SqlPredicate;
import opa.datafilter.core.ast.db.query.exception.PartialEvauationException;
import opa.datafilter.core.ast.db.query.model.response.OpaCompilerResponse;
import opa.datafilter.core.ast.db.query.model.response.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @author joffryferrater
 */
public class AstToMongoDBQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(AstToMongoDBQuery.class);

    private OpaCompilerResponse opaCompilerResponse;

    public AstToMongoDBQuery(OpaCompilerResponse opaCompilerResponse) {
        this.opaCompilerResponse = opaCompilerResponse;
    }

    public Query createQuery() {
        List<List<Predicate>> predicates = opaCompilerResponse.getResult().getQueries();
        List<Criteria> criteriaList = predicates.stream()
                .map(this::chainCriteriaByAndOperator)
                .collect(toList());
        Criteria chainedCriterias = chainCriteriaByOrOperator(criteriaList);
        return query(chainedCriterias);
    }

    Criteria chainCriteriaByOrOperator(List<Criteria> criteriaList) {
        Criteria[] criteriaArray = criteriaList.toArray(Criteria[]::new);
        if (criteriaArray.length == 0) {
            throw new PartialEvauationException("The Open Policy Agent partial evaluation has returned an empty result");
        } else if (criteriaArray.length == 1) {
            return criteriaArray[0];
        } else {
            Criteria criteria = new Criteria();
            criteria.orOperator(criteriaArray);
            return criteria;
        }
    }

    Criteria chainCriteriaByAndOperator(List<Predicate> predicates) {
        List<SqlPredicate> sqlPredicates = predicates.stream()
                .map(predicate -> new PredicateConverter(predicate).astToSqlPredicate())
                .collect(toList());
        Criteria result = new Criteria();
        if (sqlPredicates.isEmpty()) {
            return result;
        }
        SqlPredicate firstIndexSqlPredicate = sqlPredicates.get(0);
        Criteria whereCriteria = initialCriteria(firstIndexSqlPredicate);
        int size = sqlPredicates.size();
        //If there is only one predicate return the 'where' criteria
        if (size == 1) {
            return whereCriteria;
        }
        //Add the succeeding criteria to 'and' operation
        for (int x = 1; x < size; x++) {
            SqlPredicate sqlPredicate = sqlPredicates.get(x);
            result = andCriteria(sqlPredicate, whereCriteria);
        }
        return result;
    }

    Criteria initialCriteria(SqlPredicate sqlPredicate) {
        String attributeName = getAttributeName(sqlPredicate);
        Criteria criteria = where(attributeName);
        criteria = createComparisonCriteria(sqlPredicate, criteria);
        return criteria;
    }

    Criteria andCriteria(SqlPredicate sqlPredicate, Criteria whereCriteria) {
        String attributeName = getAttributeName(sqlPredicate);
        Criteria criteria = whereCriteria.and(attributeName);
        return createComparisonCriteria(sqlPredicate, criteria);
    }

    private String getAttributeName(SqlPredicate sqlPredicate) {
        String leftExpression = sqlPredicate.getLeftExpression();
        return leftExpression.split("\\.")[1];
    }

    private Criteria createComparisonCriteria(SqlPredicate sqlPredicate, Criteria criteria) {
        String operator = sqlPredicate.getOperator();
        Object rightExpression = sqlPredicate.getRightExpression();
        switch (operator) {
            case "=":
                criteria = criteria.is(rightExpression);
                break;
            case "<":
                criteria = criteria.lt(rightExpression);
                break;
            case "<=":
                criteria = criteria.lte(rightExpression);
                break;
            case ">":
                criteria = criteria.gt(rightExpression);
                break;
            case ">=":
                criteria = criteria.gte(rightExpression);
                break;
            default:
                LOGGER.warn("Mongo DB criteria equivalent of SQL operator '{}' is not yet supported", operator);
        }
        return criteria;
    }
}
