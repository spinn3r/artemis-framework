package com.spinn3r.artemis.time.init;

import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.time.TimeZones;

import java.util.TimeZone;

/**
 * Set the Java default timezone to UTC
 */
public class UTCDefaultTimezoneService extends BaseService {

    @Override
    public void init() {
        TimeZone.setDefault( TimeZones.UTC );
    }

}
