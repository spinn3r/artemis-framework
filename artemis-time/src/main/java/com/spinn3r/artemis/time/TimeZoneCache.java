package com.spinn3r.artemis.time;

import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class TimeZoneCache {

    private static Map<String, TimeZone> cache = new ConcurrentHashMap<>();

    /**
     * Get a TimeZone from the valid system TimeZone cache.  This is totally
     * lock free so much higher performance than java.util.TimeZone.  Further,
     * it will return null if it's an invalid timezone.
     */
    public static TimeZone getTimeZone(String name) {
        return cache.get( name );
    }

    // ** load up all timezones before we startup so that they are in the cache.
    static {

        //init the built in system timezones.  GMT and UTC are included.
        String[] zones = TimeZone.getAvailableIDs();

        for (String zone : zones) {
            updateCacheForZone( zone );
        }

        for (int hour = 0; hour <= 24; ++hour) {

            //this is about 6k TimeZones for ever minute (24*60*4
            for (int minute = 0; minute < 60; ++minute) {
                updateCacheForZone( String.format( "GMT+%02d:%02d", hour, minute ) );
                updateCacheForZone( String.format( "GMT+%02d%02d",  hour, minute ) );
                updateCacheForZone( String.format( "GMT-%02d:%02d", hour, minute ) );
                updateCacheForZone( String.format( "GMT-%02d%02d",  hour, minute ) );
            }

            //GMT can just be GMT+01 (hours only) so we should support this.
            updateCacheForZone( String.format( "GMT+%02d", hour ) );
            updateCacheForZone( String.format( "GMT-%02d", hour ) );

            //further (ug) GMT can be without a zero padd before the hours.
            updateCacheForZone( String.format( "GMT+%d", hour ) );
            updateCacheForZone( String.format( "GMT-%d", hour ) );

        }

        //zulu used by ISO 8601
        cache.put( "Z", TimeZone.getTimeZone( "UTC" ) );
    }

    private static void updateCacheForZone(String zone) {

        cache.put( zone, TimeZone.getTimeZone( zone ) );

    }

}