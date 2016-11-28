package com.spinn3r.artemis.json;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.spinn3r.artemis.util.misc.TextFiles;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * JSON stream parser with one entry per line.
 */
public class JSONS {

    public static <T> ImmutableList<T> parse( Class<T> clazz, InputStream inputStream) throws IOException {

        List<T> result = Lists.newArrayList();

        String text = TextFiles.toUTF8(inputStream);

        for (String line : text.split("\n")) {

            T record= JSON.fromJSON(clazz, line);
            result.add(record);
        }

        return ImmutableList.copyOf(result);

    }

}
