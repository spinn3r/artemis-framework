package com.spinn3r.artemis.util.misc;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class HistographFormatter {

    /**
     * Create an ASCII art chart of the given histograph.
     */
    public static <K extends Comparable<K>> String chart( Histograph<K> histograph ) {

        StringBuilder buff = new StringBuilder();

        Map<K,AtomicInteger> entries = histograph.read();

        if ( entries.size() == 0 ) {
            return buff.toString();
        }

        double max = 0;
        double sum = 0;
        for (Map.Entry<K, AtomicInteger> entry : entries.entrySet() ) {
            max = Math.max( max, entry.getValue().get() );
            sum += entry.getValue().get();
        }

        int max_columns = 80;

        buff.append( "HISTOGRAPH:\n" );

        for (Map.Entry<K, AtomicInteger> entry : entries.entrySet() ) {

            K key = entry.getKey();
            AtomicInteger value = entry.getValue();

            double perc_of_max = value.get() / max;
            int width = (int)(perc_of_max * max_columns);

            double perc = 100 * (value.get() / sum);

            buff.append( String.format( " %-10s  %2.2f%%: %s (%s)\n", key, perc, Strings.padding( '*', width ), value.get() ) );

        }

        return buff.toString();

    }

}
