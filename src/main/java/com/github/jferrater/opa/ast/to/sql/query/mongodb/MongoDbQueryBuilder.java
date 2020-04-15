package com.github.jferrater.opa.ast.to.sql.query.mongodb;

import com.github.jferrater.opa.ast.to.sql.query.core.elements.SqlPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;

public class MongoDbQueryBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDbQueryBuilder.class);

    Criteria convertSqlPredicateToMongoDbCriteria(SqlPredicate sqlPredicate) {
        Criteria criteria = Criteria.where(sqlPredicate.getLeftExpression());
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
                LOGGER.warn("Mongod DB criteria equivalent of operator '{}' is not yet supported", operator);
        }
        return criteria;
    }
}
