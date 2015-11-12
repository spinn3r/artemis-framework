package com.spinn3r.artemis.jcommander;

import com.beust.jcommander.JCommander;
import com.spinn3r.artemis.util.misc.Strings;

import java.util.List;

/**
 *
 */
public class Args {

    public static <T> T  parse( Class<T> clazz, List<String> args ) {
        return parse( clazz, Strings.toArray( args ) );
    }

    public static <T> T parse( Class<T> clazz, String... args ) {

        try {
            T result = clazz.newInstance();
            JCommander jc = new JCommander( result );
            jc.parse( args );
            return result;
        } catch (InstantiationException|IllegalAccessException e) {
            throw new RuntimeException( e );
        }

    }

}
