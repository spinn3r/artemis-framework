package com.spinn3r.artemis.util.math;

/**
 * Safely compute percentages between two values and avoid issues like int
 * to double conversion, divide by zero, etc.
 */
public class Percentage {

    /**
     * Compute the percentage and return as a formatted string.  This is done
     * safely with any errors returned as strings.
     *
     * @param hits
     * @param total
     * @return
     */
    public static String format( double hits , double total ) {

        try {
            double perc = perc( hits, total );

            return String.format( "%3.2f%%", perc );
        } catch (NoPercentException e) {
            return e.getMessage();
        }

    }

    /**
     * Return a percentage between 0 and 100 for the given
     *
     * @param hits
     * @param total
     * @return
     * @throws NoPercentException
     */
    public static double perc( double hits, double total ) throws NoPercentException {

        if ( total == 0 ) {
            throw new NoPercentException( "none (total=zero)" );
        }

        if ( hits > total ) {
            throw new NoPercentException( "none (hits > total)" );
        }

        return 100 * ( hits / total );

    }

}
