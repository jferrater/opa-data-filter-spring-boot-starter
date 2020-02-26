package com.joffryferrater.opadatafilterspringbootstarter.core;

import com.joffryferrater.opadatafilterspringbootstarter.core.elements.SqlPredicate;
import com.joffryferrater.opadatafilterspringbootstarter.core.util.SqlUtil;
import com.joffryferrater.opadatafilterspringbootstarter.model.response.Predicate;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class AstToSql {

    String andConstraints(List<Predicate> predicates) {
        List<SqlPredicate> sqlPredicates = predicates.stream()
                .map(predicate -> new PredicateConverter(predicate).astToSqlPredicate()).
                        collect(toList());
        return SqlUtil.concatByAndOperation(sqlPredicates);
    }
}
