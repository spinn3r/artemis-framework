package com.spinn3r.artemis.network;

import com.spinn3r.log5j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class Encodings {

    private static Pattern metaPattern = Pattern.compile( "(?mis)<meta[^>]+>" );

    //used to make sure this is an actual Content-Type meta.
    private static Pattern httpEquivPattern = Pattern.compile( "(?mis)http-equiv=[\"']Content-Type[\"']" );

    //used to make sure this is an actual Content-Type meta.
    private static Pattern metaCharsetPattern = Pattern.compile( "(?mis)charset=[\"']([^\"']+)[\"']" );

    //used to fetch the actual content.
    private static Pattern contentAttributePattern = Pattern.compile( "(?mis)content=[\"']([^\"']+)[\"']" );

    private static Logger log = Logger.getLogger();

    public static String parseMetaContentType( byte[] data ) {

        try {

            //NOTE the performance of this could be improved by terminating the
            //parse when we get to <body>

            String content = new String( data, "ISO-8859-1" );

            // Need to parse out:
            //
            // HTML 4.0:
            //
            // <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
            // <meta content="text/html; charset=UTF-8" http-equiv="content-type"/>
            // <meta content="text/html; charset=UTF-8" http-equiv="content-type"/>
            //
            // HTML 5.0:
            //
            // http://www.w3schools.com/tags/att_meta_charset.asp
            //
            // <meta charset="UTF-8">
            //
            // The charset attribute is new in HTML5, and replaces the need
            // for:
            //
            // <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            //
            // Specifying the character-set using the http-equiv attribute is
            // still allowed, but the new way requires less code.

            Matcher metaMatcher = metaPattern.matcher( content );

            while ( metaMatcher.find() ) {

                String meta = metaMatcher.group( 0 );

                Matcher httpEquivMatcher = httpEquivPattern.matcher( meta );

                if ( httpEquivMatcher.find() ) {

                    Matcher contentAttributeMatcher = contentAttributePattern.matcher( meta );

                    if ( contentAttributeMatcher.find() ) {
                        return parseCharsetFromContentType( contentAttributeMatcher.group( 1 ) );
                    }

                }

                Matcher metaCharsetMatcher = metaCharsetPattern.matcher( meta );

                if ( metaCharsetMatcher.find() ) {
                    return SafeCharsetLookup.lookup( metaCharsetMatcher.group( 1 ) );
                }

            }

        } catch ( Exception t ) {
            log.error( "Could not parse meta content type: ", t );
        }

        return null;

    }

    public static String parseCharsetFromContentType( String contentType ) {

        if ( contentType == null )
            return null;

        Pattern p = Pattern.compile( "(?i)charset=[\"']?([^ \"';,]+)" );
        Matcher m = p.matcher( contentType );

        if ( m.find() ) {

            String result = m.group( 1 );

            result = SafeCharsetLookup.lookup( result );

            return result;
        }

        return null;

    }

}
