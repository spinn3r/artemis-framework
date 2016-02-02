package com.spinn3r.artemis.util.text;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 *
 */
public class TableFormatter {

    /**
     * Format a given table of strings as a table of text.
     * @param table
     * @return
     */
    public static String format( List<List<String>> table ) {

        StringBuilder buff = new StringBuilder();

        if ( table.size() == 0 ) {
            // we're done.
            return buff.toString();
        }

        List<Integer> widths = widths( table );

        for (List<String> row : table) {

            for (int i = 0; i < row.size(); i++) {
                String value = row.get( i );

                int len = widths.get( i );

                buff.append( String.format( fmt( len ), value ) );
                buff.append( " " );

            }

            buff.append( "\n" );

        }

        return buff.toString();
    }

    private static List<String> column( List<List<String>> table, int column ) {

        List<String> result = Lists.newArrayList();

        for (List<String> strings : table) {
            result.add( strings.get( column ) );
        }

        return result;

    }

    private static List<Integer> widths( List<List<String>> table ) {

        List<Integer> result = Lists.newArrayList();

        List<String> first = table.get( 0 );

        for (int i = 0; i < first.size(); i++) {
            result.add( maxLen( column( table, i ) ) );
        }

        return result;

    }

    private static int maxLen( Collection<String> values ) {

        int result = 0;

        for (String value : values) {
            if ( value.length() > result ) {
                result = value.length();
            }
        }

        return result;

    }

    private static String fmt( int len ) {
        return "%-" + len + "s";
    }

}
