package com.github.jferrater.opa.ast.to.sql.query.mongodb;

import com.github.jferrater.opa.ast.to.sql.query.core.elements.SqlPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public class MongoDbQueryBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDbQueryBuilder.class);

    Criteria buildAndCriteriaFromSqlPredicates(List<SqlPredicate> sqlPredicates) {
        Criteria result = new Criteria();
        if(sqlPredicates.isEmpty()) {
            return result;
        }
        SqlPredicate firstIndexSqlPredicate = sqlPredicates.get(0);
        Criteria whereCriteria = whereCriteria(firstIndexSqlPredicate);
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

    Criteria whereCriteria(SqlPredicate sqlPredicate) {
        Criteria criteria = Criteria.where(sqlPredicate.getLeftExpression());
        criteria = getComparisonOperator(sqlPredicate, criteria);
        return criteria;
    }

    Criteria andCriteria(SqlPredicate sqlPredicate, Criteria whereCriteria) {
        Criteria criteria = whereCriteria.and(sqlPredicate.getLeftExpression());
        return getComparisonOperator(sqlPredicate, criteria);
    }

    private Criteria getComparisonOperator(SqlPredicate sqlPredicate, Criteria criteria) {
        String operator = sqlPredicate.getOperator();
        String rightExpression = sqlPredicate.getRightExpression();
        switch (operator) {
            case "=":
                criteria = criteria.is(rightExpression);
                break;
            case "<":
                criteria = criteria.lt(rightExpression);
                break;
            case ">":
                criteria = criteria.gt(rightExpression);
                break;
            default:
                LOGGER.warn("Mongo DB criteria equivalent of SQL operator '{}' is not yet supported", operator);
        }
        return criteria;
    }
}
