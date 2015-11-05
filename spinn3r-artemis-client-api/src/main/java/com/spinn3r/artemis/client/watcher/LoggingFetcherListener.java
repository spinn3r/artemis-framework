package com.spinn3r.artemis.client.watcher;

import com.spinn3r.artemis.client.json.Content;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Example fetcher listener that just logs the title and link to log4j.
 */
public class LoggingFetcherListener implements FetcherListener {

    public static TimeZone UTC = TimeZone.getTimeZone("UTC");

    private static final Logger log = Logger.getLogger( LoggingFetcherListener.class );

    @Override
    public void onContent(Parse parse) {

        for (Content content : parse.getContent()) {

            String dateFound = null;

            if ( content.getDateFound() != null ) {
                dateFound = toISO8601( content.getDateFound() );
            }

            log.info( String.format( "Found content date_found=%s, title=%s, permalink=%s",
                                     dateFound,
                                     content.getTitle(),
                                     content.getPermalink() ) );
        }

    }

    // copied from artemis-time to avoid having a dependency with artemis-time.
    private static String toISO8601(Date date) {
        // NOTE: Simple Date Format is NOT thread safe.
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone( UTC );
        return df.format( date );
    }

}
