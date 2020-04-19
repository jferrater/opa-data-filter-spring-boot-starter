package com.github.jferrater.opa.ast.db.query.mongodb;

import com.github.jferrater.opa.ast.db.query.core.PredicateConverter;
import com.github.jferrater.opa.ast.db.query.core.elements.SqlPredicate;
import com.github.jferrater.opa.ast.db.query.model.response.OpaCompilerResponse;
import com.github.jferrater.opa.ast.db.query.model.response.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

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

    private Criteria chainCriteriaByOrOperator(List<Criteria> criteriaList) {
        Criteria[] criteriaArray = criteriaList.toArray(Criteria[]::new);
        Criteria criteria = new Criteria();
        criteria.orOperator(criteriaArray);
        String toJson = criteria.getCriteriaObject().toJson();
        LOGGER.info("Query document in json format:\n{}", toJson);
        return criteria;
    }

    Criteria chainCriteriaByAndOperator(List<Predicate> predicates) {
        List<SqlPredicate> sqlPredicates = predicates.stream()
                .map(predicate -> new PredicateConverter(predicate).astToSqlPredicate())
                .collect(toList());
        Criteria result = new Criteria();
        if(sqlPredicates.isEmpty()) {
            return result;
        }
        SqlPredicate firstIndexSqlPredicate = sqlPredicates.get(0);
        Criteria whereCriteria = initialCriteria(firstIndexSqlPredicate);
        int size = sqlPredicates.size();
        //If there is only one predicate return the 'where' criteria
        if(size == 1) {
            return whereCriteria;
        }
        //Add the succeeding criteria to 'and' operation
        for(int x = 1; x < size; x++) {
            SqlPredicate sqlPredicate = sqlPredicates.get(x);
            result = andCriteria(sqlPredicate, whereCriteria);
        }
        return result;
    }

    Criteria initialCriteria(SqlPredicate sqlPredicate) {
        Criteria criteria = where(sqlPredicate.getLeftExpression());
        criteria = createComparisonCriteria(sqlPredicate, criteria);
        return criteria;
    }

    Criteria andCriteria(SqlPredicate sqlPredicate, Criteria whereCriteria) {
        Criteria criteria = whereCriteria.and(sqlPredicate.getLeftExpression());
        return createComparisonCriteria(sqlPredicate, criteria);
    }

    private Criteria createComparisonCriteria(SqlPredicate sqlPredicate, Criteria criteria) {
        String operator = sqlPredicate.getOperator();
        String rightExpression = sqlPredicate.getRightExpression();
        switch (operator) {
            case "=":
                criteria = criteria.is(rightExpression);
                break;
            case "<":
                criteria = criteria.lt(Integer.valueOf(rightExpression));
                break;
            case ">":
                criteria = criteria.gt(Integer.valueOf(rightExpression));
                break;
            default:
                LOGGER.warn("Mongo DB criteria equivalent of SQL operator '{}' is not yet supported", operator);
        }
        return criteria;
    }
}
