package com.github.jferrater.opa.ast.db.query.sql;

import com.github.jferrater.opa.ast.db.query.core.PredicateConverter;
import com.github.jferrater.opa.ast.db.query.core.elements.LogicalOperation;
import com.github.jferrater.opa.ast.db.query.core.elements.SqlPredicate;
import com.github.jferrater.opa.ast.db.query.core.util.SqlUtil;
import com.github.jferrater.opa.ast.db.query.exception.PartialEvauationException;
import com.github.jferrater.opa.ast.db.query.model.request.PartialRequest;
import com.github.jferrater.opa.ast.db.query.model.response.OpaCompilerResponse;
import com.github.jferrater.opa.ast.db.query.model.response.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class AstToSql {

    private OpaCompilerResponse opaCompilerResponse;

    public AstToSql(OpaCompilerResponse opaCompilerResponse) {
        this.opaCompilerResponse = opaCompilerResponse;
    }

    public String getSqlQueryStatements(PartialRequest partialRequest) {
        List<String> unknowns = new ArrayList<>(partialRequest.getUnknowns());
        return sqlQueries(unknowns);
    }

    public String getSqlQueryStatements(Set<String> opaPartialRequestUnknownProperty) {
        List<String> unknowns = new ArrayList<>(opaPartialRequestUnknownProperty);
        return sqlQueries(unknowns);
    }

    private String sqlQueries(List<String> unknowns) {
        SqlStatement sqlStatement = new SqlStatement.Builder()
                .select("*")
                .from(unknowns.stream().map(SqlUtil::getTableName).collect(toList()))
                .where(whereClauseConstraints())
                .build();
        return sqlStatement.getExecutableSqlStatements();
    }

    private String whereClauseConstraints() {
        List<List<Predicate>> queries = opaCompilerResponse.getResult().getQueries();
        if(queries.isEmpty()) {
            throw new PartialEvauationException("OPA partial evaluation result queries is empty");
        }
        List<String> predicatesInString = queries.stream().map(this::andOperationConstraints).collect(toList());
        String concatByOrOperation = SqlUtil.concat(predicatesInString, " " + LogicalOperation.OR.name() + " ");
        return concatByOrOperation == null ? "" : concatByOrOperation;
    }

    String andOperationConstraints(List<Predicate> predicates) {
        List<SqlPredicate> sqlPredicates = predicates.stream()
                .map(predicate -> new PredicateConverter(predicate).astToSqlPredicate()).
                        collect(toList());
        return SqlUtil.concatByAndOperation(sqlPredicates);
    }
}
