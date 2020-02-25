package com.joffryferrater.opadatafilterspringbootstarter.core.util;

import java.util.List;

public class SqlUtil {

    private SqlUtil() {
    }

    public static String concat(List<String> strings) {
        if (strings.isEmpty()) return null;
        int size = strings.size();
        int i = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : strings) {
            stringBuilder.append(s);
            if (i != size - 1) stringBuilder.append(", ");
            i++;
        }
        return stringBuilder.toString();
    }
}
