package com.spinn3r.artemis.init;

import com.spinn3r.artemis.time.SystemClock;

/**
 *
 */
public class TestingFrameworks {

    public static boolean isTesting() {

        return System.getProperty("java.class.path").contains("junit");

    }

}
