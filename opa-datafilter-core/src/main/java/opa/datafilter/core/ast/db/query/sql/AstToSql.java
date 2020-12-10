package opa.datafilter.core.ast.db.query.sql;

import opa.datafilter.core.ast.db.query.PredicateConverter;
import opa.datafilter.core.ast.db.query.elements.LogicalOperation;
import opa.datafilter.core.ast.db.query.elements.SqlPredicate;
import opa.datafilter.core.ast.db.query.model.request.PartialRequest;
import opa.datafilter.core.ast.db.query.model.response.OpaCompilerResponse;
import opa.datafilter.core.ast.db.query.model.response.Predicate;
import opa.datafilter.core.ast.db.query.util.SqlUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toList;

/**
 * @author joffryferrater
 */
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
        List<String> predicatesInString = queries.stream().map(this::andOperationConstraints).collect(toList());
        String concatByOrOperation = SqlUtil.concat(predicatesInString, " " + LogicalOperation.OR.name() + " ");
        return concatByOrOperation == null ? "" : concatByOrOperation;
    }

    String andOperationConstraints(List<Predicate> predicates) {
        List<SqlPredicate> sqlPredicates = predicates.stream()
                .map(predicate -> new PredicateConverter(predicate).astToSqlPredicate())
                .filter(Objects::nonNull)
                .collect(toList());
        return SqlUtil.concatByAndOperation(sqlPredicates);
    }
}
