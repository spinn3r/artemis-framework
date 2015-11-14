package com.spinn3r.artemis.jcommander;

import com.beust.jcommander.JCommander;

/**
 *
 */
public class Usages {

    public static String usage( JCommander jc ) {

        StringBuilder buff = new StringBuilder();
        jc.usage( buff );
        return buff.toString();

    }

    public static String usage( JCommander jc, String command ) {

        StringBuilder buff = new StringBuilder();
        jc.usage( command, buff );
        return buff.toString();

    }

}
