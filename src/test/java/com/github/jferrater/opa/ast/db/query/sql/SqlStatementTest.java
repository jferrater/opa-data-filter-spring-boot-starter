package com.github.jferrater.opa.ast.db.query.sql;

import com.github.jferrater.opa.ast.db.query.sql.SqlStatement;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class SqlStatementTest {

    @Test
    void shouldBuildSqlStatement() {
        List<String> columns = List.of("name", "address");
        List<String> tables = List.of("employees");
        String predicatesInString = "(employees.name = 'Joffer' AND employees.address = 'Stockholm')";
        SqlStatement target = new SqlStatement.Builder()
                .select(columns)
                .from(tables)
                .where(predicatesInString)
                .build();
        assertThat(target.getExecutableSqlStatements(), is("SELECT name, address FROM employees WHERE (employees.name = 'Joffer' AND employees.address = 'Stockholm');"));
    }

    @Test
    void shouldSelectFromAllColumns() {
        List<String> columns = List.of("*");
        List<String> tables = List.of("employees");
        String predicatesInString = "(employees.name = 'Joffer' AND employees.address = 'Stockholm')";
        SqlStatement target = new SqlStatement.Builder()
                .select(columns)
                .from(tables)
                .where(predicatesInString)
                .build();
        assertThat(target.getExecutableSqlStatements(), is("SELECT * FROM employees WHERE (employees.name = 'Joffer' AND employees.address = 'Stockholm');"));
    }

}