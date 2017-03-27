package com.spinn3r.artemis.time;

import com.spinn3r.artemis.time.sequence.SequenceReference;
import com.spinn3r.artemis.time.sequence.SequenceReferences;

import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Convert a time reference, either in relative time, or absolute time,
 * in ISO8601 to a unix time.
 */
public class TimeReference {

    private Clock clock;

    private long value;

    private String timestamp;

    public TimeReference(Clock clock, String timestamp ) {
        this.clock = clock;
        this.timestamp = timestamp;
        this.value = parse( timestamp );
    }

    public long getValue() {
        return value;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public SequenceReference toSequenceReference() {
        return SequenceReferences.fromDate( new Date( value ) );
    }

    @Override
    public String toString() {
        return String.format( "%,d ms AKA %s", value, timestamp );
    }

    protected long parse( String timestamp ) {

        if ( timestamp.startsWith( "+" ) || timestamp.startsWith( "-" ) ) {

            Pattern pattern = Pattern.compile( "([+-])([0-9]+)(day|hour|minute)s?" );
            Matcher matcher = pattern.matcher( timestamp );

            if ( ! matcher.find() ) {
                throw new RuntimeException( "time reference input invalid: " + timestamp );
            }

            String direction = matcher.group(1);
            int duration = Integer.parseInt( matcher.group(2) );
            String unit = matcher.group(3);

            long converted = 0;

            if ( "day".equals( unit ) ) {

                converted = TimeUnit.MILLISECONDS.convert( duration, TimeUnit.DAYS );

            } else if ( "hour".equals( unit ) ) {

                converted = TimeUnit.MILLISECONDS.convert( duration, TimeUnit.HOURS );

            } else if ( "minute".equals( unit ) ) {

                converted = TimeUnit.MILLISECONDS.convert( duration, TimeUnit.MINUTES );

            } else {
                throw new RuntimeException( "Invalid unit" );
            }

            if ( "-".equals( direction ) ) {
                converted = converted * -1;
            }

            return clock.currentTimeMillis() + converted;

        } else {

            try {

                return ISO8601.parse( timestamp ).getDuration();

            } catch (ParseException e) {
                throw new RuntimeException( "Failed to parse ISO time: " + timestamp, e );
            }

        }

    }

    public static void main(String[] args) {

        Clock clock = new SystemClock();

        TimeReference timeReference = new TimeReference( clock, "-1hour" );

        System.out.printf( "%s\n", timeReference.getValue() );

    }

}

