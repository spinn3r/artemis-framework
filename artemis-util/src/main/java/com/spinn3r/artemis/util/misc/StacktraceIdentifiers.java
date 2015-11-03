package com.spinn3r.artemis.util.misc;

import com.spinn3r.artemis.util.crypto.SHA1;

/**
 *
 *
 */
public class StacktraceIdentifiers {

    public static String computeIdentifier(Throwable throwable) {

        // TODO: include the causes (recursively) as well...
        return computeIdentifier( throwable.getStackTrace() );

    }

    public static String computeIdentifier(StackTraceElement... stackTraceElements) {

        return Base64.encode( SHA1.encode( format( stackTraceElements ) ) );

    }

    public static String format( StackTraceElement... stackTraceElements ) {

        StringBuilder buff = new StringBuilder();

        for (StackTraceElement stackTraceElement : stackTraceElements ) {
            buff.append( stackTraceElement );
            buff.append( "\n" );
        }

        return buff.toString();

    }
}
