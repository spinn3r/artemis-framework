package com.spinn3r.artemis.network.init;

import sun.util.logging.PlatformLogger;

/**
 *
 */
public class NetworkSupport {

    public static void disablePlatformLogger() {
        // used so that we don't constantly log cookie information which isn't necessary.
        PlatformLogger.getLogger("java.net.CookieManager").setLevel(PlatformLogger.Level.OFF);
    }

}
