package com.spinn3r.artemis.time;

/**
 * References to various versions fo synthetic clocks.
 */
public class SyntheticClocks {

    /**
     * Return a synthetic clock modeled after a date in recent time.  This is
     * useful for debugging values which aren't around unix epoch.
     *
     * @return
     */
    public static SyntheticClock recentClock() {
        return new SyntheticClock( 1403399332247L );
    }

}
