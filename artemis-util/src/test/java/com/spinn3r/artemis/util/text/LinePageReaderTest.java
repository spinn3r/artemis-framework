package com.spinn3r.artemis.util.text;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.spinn3r.artemis.util.primitives.Integers;
import javafx.util.converter.IntegerStringConverter;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static java.awt.SystemColor.text;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class LinePageReaderTest {

    @Test
    public void testEmpty() throws Exception {

        assertEquals("[]", read("", 1).toString());

    }

    @Test
    public void testOneLine() throws Exception {

        assertEquals("[[line0]]",
                     read(createLines(1), 1).toString());

        assertEquals("[[line0]]",
                     read(createLines(1), 10).toString());

    }


    @Test
    public void testTenLines() throws Exception {

        assertEquals("[[line0], [line1], [line2], [line3], [line4], [line5], [line6], [line7], [line8], [line9]]",
                     read(createLines(10), 1).toString());

        assertEquals("[[line0, line1], [line2, line3], [line4, line5], [line6, line7], [line8, line9]]",
                     read(createLines(10), 2).toString());

        assertEquals("[[line0, line1, line2, line3, line4], [line5, line6, line7, line8, line9]]",
                     read(createLines(10), 5).toString());

        assertEquals("[[line0, line1, line2, line3, line4, line5, line6, line7, line8, line9]]",
                     read(createLines(10), 10).toString());

    }

    private String createLines(int count) {

        StringBuilder buff = new StringBuilder();

        for (int i = 0; i < count; i++) {
            buff.append("line" + i);
            buff.append("\n");
        }

        return buff.toString();

    }

    // read in all test data for the given data set...
    private ImmutableList<ImmutableList<String>> read(String text, int pageSize) throws IOException {

        List<ImmutableList<String>> result = Lists.newArrayList();

        LinePageReader linePageReader = new LinePageReader(Charsets.UTF_8, pageSize, () -> toInputStream(text));

        LinePageIterator linePageIterator = linePageReader.iterator();

        while(linePageIterator.hasNext()) {
            result.add(linePageIterator.next());
        }

        return ImmutableList.copyOf(result);

    }

    private InputStream toInputStream(String text) {
        return new ByteArrayInputStream(text.getBytes(Charsets.UTF_8));
    }

}