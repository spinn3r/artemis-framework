package com.spinn3r.artemis.test.logging;

import org.apache.log4j.xml.DOMConfigurator;

/**
 *
 */
public class Logging {

    /**
     * Create a simple and easy logger that just writes to the console.
     */
    public static void toConsole() {
        DOMConfigurator.configure( Logging.class.getResource( "/log4j-stdout.xml" ) );
    }

}
