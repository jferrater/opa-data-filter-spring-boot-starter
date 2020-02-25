package com.joffryferrater.opadatafilterspringbootstarter.core.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class SqlUtilTest {

    @Test
    void shouldConcatenateSetOfStrings() {
        List<String> strings = List.of("name", "address");
        String result = SqlUtil.concat(strings);
        assertThat(result, is("name, address"));
    }
}