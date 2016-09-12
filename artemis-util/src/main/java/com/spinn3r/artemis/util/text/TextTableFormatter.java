package com.spinn3r.artemis.util.text;

import com.google.common.collect.Lists;
import com.spinn3r.artemis.util.misc.Strings;

import java.util.Arrays;
import java.util.List;

/**
 * Computes a format of text with headers and fixed width based on the inputs.
 */
public class TextTableFormatter {

    protected List<List<String>> rows = Lists.newArrayList();

    private TextTableFormatter() {
    }

    public String format() {
        return TableFormatter.format(rows);
    }

    protected void headings(String... headings) {

        rows.add(Arrays.asList(headings));

        List<String> underlines = Lists.newArrayList();

        for (String heading : headings) {
            underlines.add(Strings.repeat("-", heading.length()));
        }

        rows.add(underlines);

    }

    public static TextTableFormatter1 forHeadings(String col0) {
        TextTableFormatter1 result = new TextTableFormatter1();
        result.headings(col0);
        return result;
    }

    public static TextTableFormatter2 forHeadings(String col0, String col1) {
        TextTableFormatter2 result = new TextTableFormatter2();
        result.headings(col0, col1);
        return result;
    }

    public static TextTableFormatter3 forHeadings(String col0, String col1, String col2) {
        TextTableFormatter3 result = new TextTableFormatter3();
        result.headings(col0, col1, col2);
        return result;
    }

    public static class TextTableFormatter1 extends TextTableFormatter {

        public TextTableFormatter1 row(Object col0) {
            rows.add(Lists.newArrayList(col0.toString()));
            return this;
        }

    }

    public static class TextTableFormatter2 extends TextTableFormatter {

        public TextTableFormatter2 row(Object col0, Object col1) {
            rows.add(Lists.newArrayList(col0.toString(), col1.toString()));
            return this;
        }

    }

    public static class TextTableFormatter3 extends TextTableFormatter {

        public TextTableFormatter3 row(Object col0, Object col1, Object col2) {
            rows.add(Lists.newArrayList(col0.toString(), col1.toString(), col2.toString()));
            return this;
        }

    }



}
