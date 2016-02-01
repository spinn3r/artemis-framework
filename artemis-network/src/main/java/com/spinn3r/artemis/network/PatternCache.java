package com.spinn3r.artemis.network;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple cache for regexp patterns for improving performance.
 *
 */
public class PatternCache {

    static ThreadLocal<HashMap<String,Pattern>> cache = new ThreadLocal<HashMap<String,Pattern>>() {
        protected synchronized HashMap<String,Pattern> initialValue() {
            return new HashMap<>();
        }
    };

    public static Pattern getPattern( String regexp ) {
        return getPattern( regexp, 0 );
    }

    public static Pattern getPattern( String regexp, int flags ) {

        HashMap<String,Pattern> patterns = cache.get();

        Pattern p = patterns.get( regexp );

        if ( p == null ) {
            p = Pattern.compile( regexp, flags );
            patterns.put( regexp, p );
        }

        return p;

    }

}

