package com.spinn3r.artemis;

import com.codahale.metrics.Counter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 *
 */
public class MetricsFormatter {

    public static ImmutableMap<String,Long> reformat( Map<String,Counter> counterMap ) {

        Map<String,Long> result = Maps.newHashMap();

        for (String key : counterMap.keySet()) {

            Counter counter = counterMap.get( key );
            result.put( key, counter.getCount() );

        }

        return ImmutableMap.copyOf( result );

    }

}
