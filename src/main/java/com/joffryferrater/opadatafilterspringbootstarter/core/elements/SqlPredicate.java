package com.joffryferrater.opadatafilterspringbootstarter.core.elements;

public class SqlPredicate {

    private String leftExpression;
    private String rightExpression;
    private String operator;

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
