package com.spinn3r.artemis.schema.core;

import java.util.Collection;

/**
 *
 */
public class MsgFormatter {

    public static String table( Collection<? extends Msg> collection ) {

        StringBuilder buff = new StringBuilder();

        for (Msg msg : collection) {
            buff.append( String.format( "%s\n", msg.toJSON() ) );
        }

        return buff.toString();

    }

}
