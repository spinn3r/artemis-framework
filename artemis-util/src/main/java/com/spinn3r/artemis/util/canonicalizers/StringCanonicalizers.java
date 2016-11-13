package com.spinn3r.artemis.util.canonicalizers;

import java.util.regex.Pattern;

/**
 *
 */
public class StringCanonicalizers {

    public static Canonicalizer<String> regexReplace(String regex, String replacement) {

        return input -> input.replaceAll(regex, replacement);

    }

}
