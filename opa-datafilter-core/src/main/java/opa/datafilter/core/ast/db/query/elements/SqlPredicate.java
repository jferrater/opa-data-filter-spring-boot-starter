package opa.datafilter.core.ast.db.query.elements;

/**
 * @author joffryferrater
 */
public class SqlPredicate {

    private String leftExpression;
    private String operator;
    private Object rightExpression;

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

    public Object getRightExpression() {
        return rightExpression;
    }

    public void setRightExpression(Object rightExpression) {
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
