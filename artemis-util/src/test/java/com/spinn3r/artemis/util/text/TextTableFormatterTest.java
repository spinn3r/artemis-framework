package com.spinn3r.artemis.util.text;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class TextTableFormatterTest {

    @Test
    public void testBasicTableLayout() throws Exception {

        TextTableFormatter.TextTableFormatter3 textTableFormatter
          = TextTableFormatter.forHeadings("cpu", "memory", "disk");

        textTableFormatter.row("100%", "2GB", "100MB/s");

        assertEquals("      cpu    memory   disk      \n" +
                       "      ---    ------   ----      \n" +
                       "1.    100%   2GB      100MB/s   \n", textTableFormatter.format());

    }

    @Test
    public void testObjectFormatting() throws Exception {
        Object val = 10000;
        assertEquals("10,000", String.format("%,d", val));
    }

    @Test
    public void testColumnFormatting() throws Exception {

        TextTableFormatter.TextTableFormatter1 textTableFormatter
          = TextTableFormatter.forHeadings("foo");

        for (int i = 0; i < 110; i++) {
            textTableFormatter.row(String.format("asdf:%s", i));
        }

        assertEquals("",
                     textTableFormatter.format());

    }


    @Test
    public void testLeftJustify() throws Exception {

        TextTableFormatter.TextTableFormatter1 textTableFormatter
          = TextTableFormatter.forHeadings("foo");

        String text = textTableFormatter.leftJustify("hello", 10);

        assertEquals("hello     ", text);

    }


    @Test
    public void testRightJustify() throws Exception {

        TextTableFormatter.TextTableFormatter1 textTableFormatter
          = TextTableFormatter.forHeadings("foo");

        String text = textTableFormatter.rightJustify("hello", 10);

        assertEquals("     hello", text);

    }

}