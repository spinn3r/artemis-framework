package com.spinn3r.artemis.time;

/*
 * Copyright 1999,2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ISO 8601 date parsing utility.  Designed for parsing the ISO subset used in
 * Dublin Core, RSS 1.0, and Atom.
 *
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id: ISO8601DateParser.java 155416 2005-02-26 13:00:10Z dirkv $
 */
public class ISO8601DateParser {

    private static Pattern PATTERN =
        Pattern.compile( "(?i)([0-9]{4}+)-([0-9]{1,2}+)-([0-9]{1,2}+)T([0-9]{2}+):([0-9]{2}+)(:([0-9]{2}+))?(\\.[0-9]+)?(Z|([+-]([0-9]{2}+):([0-9]{2}+)))?" );


    // This implements Atom ISO 8601 parser semantics.
    //
    // http://www.ietf.org/rfc/rfc4287

    // http://www.ietf.org/rfc/rfc3339.txt

    // http://www.cl.cam.ac.uk/~mgk25/iso-time.html
    //
    // http://www.intertwingly.net/wiki/pie/DateTime
    //
    // http://www.w3.org/TR/NOTE-datetime
    //
    // Different standards may need different levels of granularity in the date and
    // time, so this profile defines six levels. Standards that reference this
    // profile should specify one or more of these granularities. If a given
    // standard allows more than one granularity, it should specify the meaning of
    // the dates and times with reduced precision, for example, the result of
    // comparing two dates with different precisions.

    // The formats are as follows. Exactly the components shown here must be
    // present, with exactly this punctuation. Note that the "T" appears literally
    // in the string, to indicate the beginning of the time element, as specified in
    // ISO 8601.

    //    Year:
    //       YYYY (eg 1997)
    //    Year and month:
    //       YYYY-MM (eg 1997-07)
    //    Complete date:
    //       YYYY-MM-DD (eg 1997-07-16)
    //    Complete date plus hours and minutes:
    //       YYYY-MM-DDThh:mmTZD (eg 1997-07-16T19:20+01:00)
    //    Complete date plus hours, minutes and seconds:
    //       YYYY-MM-DDThh:mm:ssTZD (eg 1997-07-16T19:20:30+01:00)
    //    Complete date plus hours, minutes, seconds and a decimal fraction of a
    // second
    //       YYYY-MM-DDThh:mm:ss.sTZD (eg 1997-07-16T19:20:30.45+01:00)

    // where:

    //      YYYY = four-digit year
    //      MM   = two-digit month (01=January, etc.)
    //      DD   = two-digit day of month (01 through 31)
    //      hh   = two digits of hour (00 through 23) (am/pm NOT allowed)
    //      mm   = two digits of minute (00 through 59)
    //      ss   = two digits of second (00 through 59)
    //      s    = one or more digits representing a decimal fraction of a second
    //      TZD  = time zone designator (Z or +hh:mm or -hh:mm)

    public static Date parse( String input ) {

        Matcher m = PATTERN.matcher( input );

        if ( m.find() ) {

            try {

                String zone =  m.group( 9 );
                String javazone = zone;

                if ( "Z".equals( zone ) || zone == null ) {
                    javazone = "UTC";
                } else if ( zone.startsWith( "+" ) ||
                              zone.startsWith( "-" ) ) {
                    javazone = "GMT" + zone;
                } else {
                    throw new RuntimeException( "Unknown timezone: " + zone );
                }

                TimeZone tz = null;
                Calendar c = null;
                long offset = 0;

                //TODO: we might want to backport this offset change to the RFC
                //822 date parser.

                try {

                    tz = TimeZoneCache.getTimeZone( javazone );
                    c = Calendar.getInstance( tz );

                } catch ( Exception e ) {

                    //this is almost certainly an unusual timezone like -50:00
                    //so we should support it or at LEAST try.

                    tz = TimeZoneCache.getTimeZone( "UTC" );

                    // Handle timezones like: 2004-09-15T14:13:08-50:00
                    offset = computeTimezoneOffset( zone );

                    c = Calendar.getInstance( tz );

                }

                int year = toInt( m.group( 1 ) );

                c.set( Calendar.YEAR, year );
                c.set( Calendar.MONTH, toInt( m.group( 2 ) ) - 1 );
                c.set( Calendar.DAY_OF_MONTH, toInt( m.group( 3 ) ) );

                c.set( Calendar.HOUR_OF_DAY, toInt( m.group( 4 ) ) );
                c.set( Calendar.MINUTE, toInt( m.group( 5 ) ) );

                int second = toInt( m.group( 7 ) );
                //clear out the second field
                if ( second == -1 ) second = 0;

                c.set( Calendar.SECOND, second );

                // clear out milliseconds.  this is important or we'll have
                // non-deterministic results.
                c.set( Calendar.MILLISECOND, 0 );

                //now compute the timezone.

                Date result = c.getTime();

                //done so that we can apply manual timezone diffs
                result = new Date( result.getTime() + offset );

                return result;

            } catch ( RuntimeException e ) {
                throw new RuntimeException( "Could not parse ISO 8601 timestamp: " + input, e );
            }

        }

        return null;

    }

    /**
     * If this is an unusual timezone offset, compute the actual minutes off...
     *
     */
    private static long computeTimezoneOffset( String content ) {

        // http://en.wikipedia.org/wiki/ISO_8601#Time_zone_designators
        //
        // The zone designator for other time zones is specified by the offset
        // from UTC in the format ±[hh]:[mm], ±[hh][mm], or ±[hh]. So if the
        // time being described is one hour ahead of UTC (such as the time in
        // Berlin during the winter), the zone designator would be "+01:00",
        // "+0100", or simply "+01". This is appended to the time in the same
        // way that 'Z' was above. Note that the zone designator is the actual
        // offset from UTC and does not include any information on daylight
        // saving time. The zone designator or UTC offset for a user in Chicago,
        // therefore, would be "−06:00" for the winter (Central Standard Time)
        // and "−05:00" for the summer (Central Daylight Time). The following
        // times all refer to the same moment: "18:30Z", "22:30+04",
        // "1130−0700", and "15:00−03:30". Nautical time zone letters are not
        // used with the exception of Z.

        Pattern p = Pattern.compile( "([+-])([0-9][0-9])(:?([0-9][0-9]))" );
        Matcher m = p.matcher( content );

        if ( m.find() ) {

            int hours = toInt( m.group( 2 ) );
            int minutes = toInt( m.group( 4 ) );

            long result = ((hours * 60) + minutes) * 60L * 1000L;

            if ( m.group( 1 ).equals( "-" ) )
                result = result * -1;

            return result;

        }

        return 0;

    }

    public static int toInt( String v ) {
        return toInt( v, -1 );
    }

    public static int toInt( String v, int _default ) {

        if ( v == null )
            return _default;

        if ( v.equals( "" ) )
            return _default;

        return Integer.parseInt( v );
    }

    public static String toString( Date date ) {

        if ( date == null )
            return null;

        TimeZone tz = TimeZoneCache.getTimeZone( "UTC" );

        Calendar c = Calendar.getInstance( tz );

        c.setTime( date );

        String result = String.format( "%d-%02d-%02dT%02d:%02d:%02dZ",
                                       c.get( Calendar.YEAR ),
                                       c.get( Calendar.MONTH ) + 1,
                                       c.get( Calendar.DAY_OF_MONTH ),
                                       c.get( Calendar.HOUR_OF_DAY ),
                                       c.get( Calendar.MINUTE ),
                                       c.get( Calendar.SECOND ) );

        return result;

    }

}

