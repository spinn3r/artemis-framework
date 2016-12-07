package com.spinn3r.artemis.util;

import com.google.common.collect.ImmutableList;
import com.spinn3r.artemis.guava.ImmutableCollectors;
import com.spinn3r.artemis.util.text.TextTableFormatter;

import java.nio.charset.Charset;
import java.util.Set;

/**
 *
 */
public class ReportVM {

    public static void main(String[] args) {

        System.out.printf("==== properties: \n");

        System.out.printf("%s\n", createPropertiesReport());

        System.out.printf("==== charsets: \n");

        System.out.printf("%s\n", createCharsetsReport());

    }

    private static String createPropertiesReport() {

        Set<Object> propertyKeys = System.getProperties().keySet();

        ImmutableList<String> properties =
          propertyKeys
            .stream()
            .map(Object::toString)
            .sorted()
            .collect(ImmutableCollectors.toImmutableList());

        TextTableFormatter.TextTableFormatter2 textTableFormatter = TextTableFormatter.forHeadings("name", "value");

        for (String property : properties) {
            textTableFormatter.row(property, System.getProperty(property));
        }

        return textTableFormatter.format();

    }

    private static String createCharsetsReport() {

        TextTableFormatter.TextTableFormatter2 textTableFormatter = TextTableFormatter.forHeadings("name", "value");

        textTableFormatter.row("sun.jnu.encoding", System.getProperty("sun.jnu.encoding"));
        textTableFormatter.row("file.encoding", System.getProperty("file.encoding"));
        textTableFormatter.row("_default_", Charset.defaultCharset().name());

        return textTableFormatter.format();

    }

}
