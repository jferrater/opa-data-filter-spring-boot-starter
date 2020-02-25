package com.joffryferrater.opadatafilterspringbootstarter.core;

import com.joffryferrater.opadatafilterspringbootstarter.core.elements.QueryType;
import com.joffryferrater.opadatafilterspringbootstarter.core.util.SqlUtil;

import java.util.ArrayList;
import java.util.List;

public class SqlStatement {

    private SqlStatement(){}

    private List<String> columns;
    private List<String> tables;
    private List<String> predicates;

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<String> getTables() {
        return tables;
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }

    public List<String> getPredicates() {
        return predicates;
    }

    public void setPredicates(List<String> predicates) {
        this.predicates = predicates;
    }


    public String getExecutableSqlStatements() {
        String selectStatement = selectQuery();
        String fromClause = fromClause();
        return selectStatement +
                " " +
                fromClause +
                ";";
    }

    private String selectQuery() {
        String select = QueryType.SELECT;
        String columnsConcat = SqlUtil.concat(columns);
        return select.replace("{COLUMNS}", columnsConcat == null ? "*" : columnsConcat);
    }

    private String fromClause() {
        String from = QueryType.FROM;
        String tablesConcat = SqlUtil.concat(tables);
        return from.replace("{TABLES}", tablesConcat == null ? "*" : tablesConcat);
    }

    public static class Builder {

        private List<String> columns;
        private List<String> tables;

        public Builder select(List<String> columns){
            this.columns = new ArrayList<>(columns);
            return this;
        }

        public Builder select(String column){
            this.columns.add(column);
            return this;
        }

        public SqlStatement build() {
            SqlStatement sqlStatement = new SqlStatement();
            sqlStatement.columns = columns;
            sqlStatement.tables = tables;
            return sqlStatement;
        }

        public Builder from(List<String> tables) {
            this.tables = new ArrayList<>(tables);
            return this;
        }
    }
}
