package com.spinn3r.artemis.streams.regex;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Stream;

/**
 *
 */
public class Matchers {

    /**
     * Get all the matched groups as a stream.
     */
    public static Stream<String> groups(Matcher matcher) {

        List<String> result = Lists.newArrayList();

        for (int i = 0; i < matcher.groupCount(); i++) {
            result.add( matcher.group(i+1) );
        }

        return result.stream();

    }

}
