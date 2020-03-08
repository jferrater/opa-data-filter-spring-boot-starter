package com.github.jferrater.opa.ast.to.sql.query.core.elements;


public class QueryType {

    private QueryType() {
        //
    }

    public static final String SELECT = "SELECT {COLUMNS}";
    public static final String FROM = "FROM {TABLES}";
    public static final String WHERE = "WHERE %s";
}
