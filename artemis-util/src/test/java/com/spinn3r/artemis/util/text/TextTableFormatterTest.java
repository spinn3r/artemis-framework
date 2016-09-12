package com.spinn3r.artemis.util.text;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class TextTableFormatterTest {

    @Test
    public void forHeadings() throws Exception {

        TextTableFormatter.TextTableFormatter3 textTableFormatter
          = TextTableFormatter.forHeadings("cpu", "memory", "disk");

        textTableFormatter.row("100%", "2GB", "100MB/s");

        assertEquals("cpu    memory   disk      \n" +
                       "---    ------   ----      \n" +
                       "100%   2GB      100MB/s   \n", textTableFormatter.format());

    }

}