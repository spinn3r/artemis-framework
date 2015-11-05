package com.spinn3r.artemis.network;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple cache for regexp patterns for improving performance.
 *
 */
public class PatternCache {

    static ThreadLocal cache = new ThreadLocal<HashMap>() {
        protected synchronized HashMap initialValue() {
            return new HashMap();
        }
    };

    public static Pattern getPattern( String regexp ) {
        return getPattern( regexp, 0 );
    }

    public static Pattern getPattern( String regexp, int flags ) {

        HashMap patterns = (HashMap)cache.get();

        Pattern p = (Pattern)patterns.get( regexp );

        if ( p == null ) {
            p = Pattern.compile( regexp, flags );
            patterns.put( regexp, p );
        }

        return p;

    }

    /**
     * @deprecated
     */
    public static Matcher getMatcher( String regexp, int flags, String content ) {

        Pattern p = getPattern( regexp, flags );

        return p.matcher( content );

    }

}

