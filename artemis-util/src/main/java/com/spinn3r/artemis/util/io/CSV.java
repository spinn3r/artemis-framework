package com.spinn3r.artemis.util.io;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.spinn3r.artemis.util.misc.Files;
import com.spinn3r.artemis.util.misc.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Basic CSV functions for dealing with IO.
 */
public class CSV {

    public static ImmutableList<ImmutableList<String>> parse(InputStream inputStream) throws IOException {

        List<String> lines = Strings.toLines(Files.toUTF8(inputStream));

        List<ImmutableList<String>> result = Lists.newArrayList();

        for (String line : lines) {
            result.add(split(line));
        }

        return ImmutableList.copyOf(result);

    }


    public static ImmutableList<String> split(String line) {
        return ImmutableList.copyOf(Lists.newArrayList(line.split(",")));
    }

}
