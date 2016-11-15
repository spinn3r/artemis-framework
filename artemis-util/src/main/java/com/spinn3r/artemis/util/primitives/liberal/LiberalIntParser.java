package com.spinn3r.artemis.util.primitives.liberal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Liberal integer parser.
 */
public class LiberalIntParser {

    private static Pattern pattern = Pattern.compile( "([0-9]+)((?:[\\.,][0-9]{3})+)?([\\.,][0-9]{1,2})?\\s?([kKmM])?");

    /**
     * Parse an integer and take into consideration commas, any type of suffix like K, etc.

     *
     * Valid inputs include:
     *
     * 1000
     * 1,000
     * 1k
     * 1K
     * 1M
     * 1,000,000
     *
     * @param value
     * @return
     * @throws NumberFormatException
     */
    public static int parse( String value ) throws NumberFormatException {

        Matcher matcher = pattern.matcher( value );

        if ( matcher.find() ) {
            // Append left side number
            StringBuilder data = new StringBuilder(matcher.group( 1 ));

            // thousands - remove separators
            if(matcher.group(2) != null) {
                data.append(matcher.group(2).replaceAll("[\\.,]", ""));
            }

            // decimals - normalize to point
            if(matcher.group(3) != null) {
                data.append(matcher.group(3).replace(",", "."));
            }
            
            // this has to be parsed as a double because an input could be
            // 53.4M
            double result = Double.parseDouble( data.toString() );

            String suffix = matcher.group( 4 );

            if ( suffix != null ) {
                suffix = suffix.toUpperCase();

                switch( suffix ) {
                    case "K":
                        result *= 1000;
                        break;
                    case "M":
                        result *= 1000000;
                        break;
                }

            }

            return (int)result;

        }

        throw new NumberFormatException( "Not an integer: " + value );

    }

    public static Integer toInteger( String value ) {
        try {
            return parse( value );
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
