package com.joffryferrater.opadatafilterspringbootstarter.core.util;

import com.joffryferrater.opadatafilterspringbootstarter.core.elements.LogicalOperation;
import com.joffryferrater.opadatafilterspringbootstarter.core.elements.SqlPredicate;

import java.util.List;

public class SqlUtil {

    private SqlUtil() {
    }

    public static String concat(List<String> strings, String separator) {
        if (strings.isEmpty()) return null;
        int size = strings.size();
        int i = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : strings) {
            stringBuilder.append(s);
            if (i != size - 1) stringBuilder.append(separator);
            i++;
        }
        return stringBuilder.toString();
    }

    public static String concatByAndOperation(List<SqlPredicate> predicates) {
        if (predicates.isEmpty()) return null;
        int size = predicates.size();
        int i = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        for (SqlPredicate predicate : predicates) {
            stringBuilder.append(predicate.getLeftExpression());
            stringBuilder.append(" ");
            stringBuilder.append(predicate.getOperator());
            stringBuilder.append(" ");
            stringBuilder.append("'");
            stringBuilder.append(predicate.getRightExpression());
            stringBuilder.append("'");
            if (i != size - 1) {
                stringBuilder.append(" ").append(LogicalOperation.AND.name()).append(" ");
            }
            i++;
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
