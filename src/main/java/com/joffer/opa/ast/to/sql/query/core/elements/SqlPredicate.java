package com.joffer.opa.ast.to.sql.query.core.elements;

public class SqlPredicate {

    private String leftExpression;
    private String operator;
    private String rightExpression;

    public SqlPredicate() {
    }

    public SqlPredicate(String leftExpression, String operator, String rightExpression) {
        this();
        this.leftExpression = leftExpression;
        this.operator = operator;
        this.rightExpression = rightExpression;
    }

    public String getLeftExpression() {
        return leftExpression;
    }

    public void setLeftExpression(String leftExpression) {
        this.leftExpression = leftExpression;
    }

    public String getRightExpression() {
        return rightExpression;
    }

    public void setRightExpression(String rightExpression) {
        this.rightExpression = rightExpression;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "SqlPredicate{" +
                "leftExpression='" + leftExpression + '\'' +
                ", rightExpression='" + rightExpression + '\'' +
                ", operator='" + operator + '\'' +
                '}';
    }
}
